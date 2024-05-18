package com.markerhub.order.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.core.exception.HubException;
import com.markerhub.order.entity.AppOrder;
import com.markerhub.order.entity.AppRefund;
import com.markerhub.order.mapper.AppRefundMapper;
import com.markerhub.order.service.AppOrderItemService;
import com.markerhub.order.service.AppOrderService;
import com.markerhub.order.service.AppPayService;
import com.markerhub.order.service.AppRefundService;
import com.markerhub.satoken.utils.StpUserUtil;
import com.markerhub.upload.service.AppUploadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Service
public class AppRefundServiceImpl extends ServiceImpl<AppRefundMapper, AppRefund> implements AppRefundService {

	@Resource
	AppOrderService appOrderService;

	@Resource
	AppOrderItemService appOrderItemService;

	@Resource
	AppUploadService appUploadService;

	@Resource
	AppRefundMapper appRefundMapper;

	@Resource
	AppPayService appPayService;

	@Override
	public Object getOrderData(long orderId) {
		AppOrder appOrder = appOrderService.getOwnById(orderId);
		appOrder.setOrderItems(appOrderItemService.listByOrderId(orderId));

		return MapUtil.builder().put("order", appOrder).put("reasons", Arrays.asList("多拍/拍错/不想要了", "不喜欢/效果不好", "少件/发错货/未收到货", "商品与描述不符", "商品质量/故障")).put("methods", Arrays.asList("自行寄回", "上门取货", "仅退款")).build();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void apply(AppRefund appRefund, MultipartFile[] pics) {

		// 1、判断是否可以申请退货
		AppOrder appOrder = appOrderService.getOwnById(appRefund.getOrderId());
		Assert.isTrue(Arrays.asList(1, 2, 3).contains(appOrder.getOrderStatus()), "该订单不支持退款");

		// 2、根据订单状态区分处理方式、待发货的立即退款、带收货已完成的进入后台处理
		if (appOrder.getOrderStatus() == 1) {
			appOrder.setOrderStatus(6);
			appRefund.setRefundStatus(2);

			//TODO 支付宝退款处理

		} else {
			appOrder.setOrderStatus(5);
			appRefund.setRefundStatus(0);
		}

		// 图片处理
		if (pics != null && pics.length > 0) {
			try {
				List<String> picUrls = appUploadService.upload(pics, 0.5f, 0.2);
				appRefund.setImages(StrUtil.join(";", picUrls));

			} catch (IOException e) {
				e.printStackTrace();
				throw new HubException("图片上传失败");
			}
		}

		// 3、更新状态
		appRefund.setOrderSn(appOrder.getSn());
		appRefund.setUserId(StpUserUtil.getLoginIdAsLong());

		appRefund.setSn(generateRefundSn());
		appRefund.setRefundAmount(appOrder.getTotalAmount());
		appRefund.setCreated(LocalDateTime.now());
		this.save(appRefund);

		appOrderService.updateById(appOrder);
	}

	@Override
	public Object pageAdmin(Page page, AppRefund appRefund) {
		return appRefundMapper.pageWithUsername(page, new QueryWrapper<AppRefund>().eq(appRefund.getSn() != null, "sn", appRefund.getSn()).eq(appRefund.getOrderSn() != null, "order_sn", appRefund.getOrderSn()).eq(appRefund.getRefundStatus() != null, "refund_status", appRefund.getRefundStatus()).orderByDesc("created"));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void auditAdmin(AppRefund appRefund) {

		AppRefund refund = this.getById(appRefund.getId());
		AppOrder order = appOrderService.getById(refund.getOrderId());

		// 1表示退货中，3表示拒绝退货
		if (appRefund.getRefundStatus() == 1 || appRefund.getRefundStatus() == 3) {
			Assert.isTrue(refund.getRefundStatus() == 0, "状态异常");
			Assert.isTrue(appRefund.getRefundAmount() != null && StrUtil.isNotBlank(appRefund.getOperateRemark()), "退款金额或处理备注不能为空");
			Assert.isTrue(appRefund.getRefundAmount().compareTo(order.getTotalAmount()) <= 0, "退款金额不能大于订单总金额");

			refund.setRefundAmount(appRefund.getRefundAmount());
			refund.setRefundStatus(appRefund.getRefundStatus());
			refund.setOperateRemark(appRefund.getOperateRemark());

			refund.setOperatorId(StpUtil.getLoginIdAsLong());
			refund.setOperateTime(LocalDateTime.now());

			if (appRefund.getRefundStatus() == 3) {
				order.setOrderStatus(7);
				appOrderService.updateById(order);
			}

		} else if (appRefund.getRefundStatus() == 2) {
			// 2表示确认退货退款

			Assert.isTrue(refund.getRefundStatus() == 1, "状态异常");
			Assert.isTrue(StrUtil.isNotBlank(appRefund.getReceiptRemark()), "处理备注不能为空");

			refund.setReceiptRemark(appRefund.getReceiptRemark());
			refund.setRefundStatus(appRefund.getRefundStatus());

			order.setOrderStatus(6);
			order.setRefundTime(LocalDateTime.now());
			appOrderService.updateById(order);

			// 退款
			appPayService.refund(appRefund.getId());
		}

		refund.setUpdated(LocalDateTime.now());
		this.updateById(refund);
	}

	private String generateRefundSn() {
		String dateStr = DateUtil.format(new Date(), "yyMMddHHmm");
		return "T" + dateStr + RandomUtil.randomNumbers(4);
	}

}




