package com.markerhub.order.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.cartItem.entity.AppCartItem;
import com.markerhub.cartItem.dto.PreviewDto;
import com.markerhub.cartItem.feign.AppCartItemClient;
import com.markerhub.core.exception.HubException;
import com.markerhub.core.lang.Result;
import com.markerhub.order.dto.OrderDto;
import com.markerhub.order.entity.AppAddress;
import com.markerhub.order.entity.AppOrder;
import com.markerhub.order.entity.AppOrderItem;
import com.markerhub.order.entity.AppRefund;
import com.markerhub.order.mapper.AppOrderMapper;
import com.markerhub.order.mapper.AppRefundMapper;
import com.markerhub.order.mq.MqSender;
import com.markerhub.order.service.AppAddressService;
import com.markerhub.order.service.AppOrderItemService;
import com.markerhub.order.service.AppOrderService;
import com.markerhub.order.utils.KdapiSearchUtil;
import com.markerhub.product.dto.ProductQuantityDto;
import com.markerhub.product.feign.AppProductClient;
import com.markerhub.product.feign.AppSkuStockClient;
import com.markerhub.satoken.utils.StpUserUtil;
import com.markerhub.user.entity.AppUser;
import com.markerhub.user.feign.AppUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AppOrderServiceImpl extends ServiceImpl<AppOrderMapper, AppOrder> implements AppOrderService {

	@Resource
	AppAddressService appAddressService;

	@Resource
	AppCartItemClient appCartItemClient;

	@Resource
	AppUserClient appUserClient;

	@Resource
	AppOrderItemService appOrderItemService;

	@Resource
	AppSkuStockClient appSkuStockClient;

	@Resource
	MqSender mqSender;

	@Resource
	AppRefundMapper appRefundMapper;

	@Resource
	AppProductClient appProductClient;

	@Resource
	AppOrderMapper appOrderMapper;

	/**
	 * 购物车购买、立即购买的订单预览
	 *
	 * @param orderDto
	 * @return
	 */
	@Override
	public Object preview(OrderDto orderDto) {

		long userId = StpUserUtil.getLoginIdAsLong();

		// 判断参数是否完整
		Assert.isTrue((orderDto.getCartIds() != null && orderDto.getCartIds().size() > 0) || (orderDto.getProductId() != null && orderDto.getSkuId() != null && orderDto.getQuantity() != null), "参数不完整");

		// 获取收货地址
		// addressId为空，则展示默认收货地址，如果没有默认收货地址，则不展示
		AppAddress appAddress;
		if (orderDto.getAddressId() == null) {
			appAddress = appAddressService.getDefault(userId);
		} else {
			// 不为空，根据id获取收货地址
			appAddress = appAddressService.getById(orderDto.getAddressId());
			Assert.isTrue(appAddress != null && appAddress.getUserId() == userId, "配送地址异常");
		}

		// 获取购物车的信息
		List<AppCartItem> cartItems = getCartItemsByOrderDto(userId, orderDto);

		// 计算价格
		BigDecimal total = cartItems.stream().map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);

		// 优惠价、运费等信息
		BigDecimal freight = new BigDecimal("0");
		total = total.add(freight);

		return MapUtil.builder().put("address", appAddress).put("cartItems", cartItems).put("total", total).put("freight", freight).build();
	}

	@Override
	public Object create(OrderDto orderDto) {
		long userId = StpUserUtil.getLoginIdAsLong();

		// 判断收货地址
		AppAddress address = appAddressService.getById(orderDto.getAddressId());
		Assert.isTrue(address != null && address.getUserId() == userId, "收货地址异常");

		// 获取购物车或立即购买的商品信息
		List<AppCartItem> cartItems = getCartItemsByOrderDto(userId, orderDto);

		// 计算总金额
		BigDecimal total = cartItems.stream().map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);

		// 优惠价、运费等信息
		BigDecimal freight = new BigDecimal("0");
		total = total.add(freight);

		// 创建订单记录
		AppOrder appOrder = new AppOrder();
		appOrder.setUserId(userId);
		appOrder.setSn(generateOrderSn());

		appOrder.setTotalAmount(total);
		appOrder.setFreight(freight);

		// 收货人地址
		appOrder.setReceiverName(address.getName());
		appOrder.setReceiverTel(address.getTel());
		appOrder.setReceiverAddress(address.getAddress());
		appOrder.setReceiverPostalCode(address.getPostalCode());
		appOrder.setReceiverAreaCode(address.getAreaCode());

		appOrder.setOrderStatus(0);

		appOrder.setIsDelete(false);
		appOrder.setCreated(LocalDateTime.now());
		appOrder.setNote(orderDto.getNote());

		this.save(appOrder);

		// 保存商品信息快照
		List<AppOrderItem> orderItems = new ArrayList<>();
		for (AppCartItem item : cartItems) {

			AppOrderItem orderItem = new AppOrderItem();
			orderItem.setProductId(item.getProductId());
			orderItem.setProductName(item.getProductName());
			orderItem.setProductImage(item.getProductImage());
			orderItem.setProductSn(item.getProductSn());
			orderItem.setCategoryId(item.getCategoryId());
			orderItem.setPrice(item.getPrice());
			orderItem.setQuantity(item.getQuantity());

			orderItem.setSkuId(item.getSkuId());
			orderItem.setSku(item.getSku());

			orderItem.setOrderId(appOrder.getId());
			orderItem.setCreated(LocalDateTime.now());

			orderItems.add(orderItem);
		}
		appOrderItemService.saveBatch(orderItems);

		// 删除购物车中的商品
		if (orderDto.getCartIds() != null && orderDto.getCartIds().size() > 0) {
			appCartItemClient.delete(orderDto.getCartIds());
		}

		// 判断库存并扣减库存
		appSkuStockClient.reduceStock(BeanUtil.copyToList(orderItems, ProductQuantityDto.class));

		// 超时未支付则自动取消订单 - 死信队列
		mqSender.sendCancelOrderMessage(appOrder.getId());

		return appOrder.getSn();
	}

	@Override
	public AppOrder getBySn(String sn) {
		AppOrder appOrder = this.getOne(new QueryWrapper<AppOrder>().eq("sn", sn));
		Assert.notNull(appOrder, "订单不存在");
		return appOrder;
	}

	@Override
	public AppOrder getOwnBySn(String sn) {
		AppOrder appOrder = getBySn(sn);
		long userId = StpUserUtil.getLoginIdAsLong();
		Assert.isTrue(userId == appOrder.getUserId(), "无权限操作该订单");
		return appOrder;
	}

	@Override
	public AppOrder getOwnById(Long id) {
		AppOrder appOrder = this.getById(id);
		long userId = StpUserUtil.getLoginIdAsLong();
		Assert.isTrue(userId == appOrder.getUserId(), "无权限操作该订单");
		return appOrder;
	}


	@Override
	public Object getOrderCount() {
		long userId = StpUserUtil.getLoginIdAsLong();

		// 订单状态：0->待付款；1->待发货；2->待收货；3-已完成；4->已取消；5-申请退款，6->已退款，7->退款失败
		List<AppOrder> appOrders = this.list(new QueryWrapper<AppOrder>().eq("user_id", userId));

		int unPay = 0;
		int unDeli = 0;
		int unRecv = 0;
		int unRefund = 0;

		long unComment = 0;

		for (AppOrder appOrder : appOrders) {
			if (appOrder.getOrderStatus() == 0) {
				unPay++;
			} else if (appOrder.getOrderStatus() == 1) {
				unDeli++;
			} else if (appOrder.getOrderStatus() == 2) {
				unRecv++;
			} else if (appOrder.getOrderStatus() == 5) {
				unRefund++;
			}
		}

		List<Long> orderIds = appOrders.stream().filter(e -> e.getOrderStatus() == 3).map(AppOrder::getId).collect(Collectors.toList());

		if (!orderIds.isEmpty()) {
			unComment = appOrderItemService.count(new QueryWrapper<AppOrderItem>().in("order_id", orderIds).isNull("comment_id"));
		}
		return MapUtil.builder().put("unPay", unPay).put("unDeli", unDeli).put("unRecv", unRecv).put("unRefund", unRefund).put("unComment", unComment).build();
	}

	@Override
	public Object getPage(Page page, long userId, Integer status) {

		Page<AppOrder> pageData = this.page(page, new QueryWrapper<AppOrder>().eq("user_id", userId).eq(status != null, "order_status", status).orderByDesc("created"));

		for (AppOrder appOrder : pageData.getRecords()) {
			List<AppOrderItem> appOrderItems = appOrderItemService.listByOrderId(appOrder.getId());
			appOrder.setOrderItems(appOrderItems);
		}

		return pageData;
	}

	@Override
	public Object detail(Long id) {
		AppOrder appOrder = this.getOwnById(id);

		List<AppOrderItem> appOrderItems = appOrderItemService.listByOrderId(id);
		appOrder.setOrderItems(appOrderItems);

		if (Arrays.asList(5, 6).contains(appOrder.getOrderStatus())) {
			// 退款单
			AppRefund appRefund = appRefundMapper.selectOne(new QueryWrapper<AppRefund>().eq("order_id", id));
			appOrder.setAppRefund(appRefund);
		}

		return appOrder;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void cancel(Long id) {
		AppOrder appOrder = this.getOwnById(id);

		Assert.isTrue(0 == appOrder.getOrderStatus(), "无法取消该订单");

		// 释放商品库存
		List<AppOrderItem> appOrderItems = appOrderItemService.listByOrderId(id);
		appSkuStockClient.releaseStock(BeanUtil.copyToList(appOrderItems, ProductQuantityDto.class));

		appOrder.setOrderStatus(4);
		appOrder.setCompleteTime(LocalDateTime.now());
		appOrder.setUpdated(LocalDateTime.now());

		this.updateById(appOrder);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		AppOrder appOrder = this.getOwnById(id);
		Assert.isTrue(Arrays.asList(0, 3, 4, 6, 7).contains(appOrder.getOrderStatus()), "无法删除该订单");

		// 待付款的订单
		if (appOrder.getOrderStatus() == 0) {
			// 释放商品库存
			List<AppOrderItem> appOrderItems = appOrderItemService.listByOrderId(id);
			appSkuStockClient.releaseStock(BeanUtil.copyToList(appOrderItems, ProductQuantityDto.class));

			appOrder.setOrderStatus(4);
			appOrder.setCompleteTime(LocalDateTime.now());
		}

		appOrder.setUpdated(LocalDateTime.now());
		this.removeById(appOrder);
	}

	@Override
	public Object getDeliveryInfo(AppOrder appOrder) throws Exception {
		String deliveryInfo = new KdapiSearchUtil().getDeliveryInfo(appOrder.getDeliveryCompany(), appOrder.getDeliverySn());

		JSONArray traces = JSONUtil.parseObj(deliveryInfo).getJSONArray("Traces");

		List<Map<Object, Object>> list = traces.stream().map(e -> {
			JSONObject obj = JSONUtil.parseObj(e);
			return MapUtil.builder().put("acceptTime", obj.getStr("AcceptTime")).put("acceptStation", obj.getStr("AcceptStation")).put("remark", obj.getStr("Remark")).build();
		}).collect(Collectors.toList());

		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void confirm(Long id) {
		AppOrder appOrder = this.getOwnById(id);
		Assert.isTrue(appOrder.getOrderStatus() == 2, "该订单无法确认收货");

		// 更新商品销量
		List<AppOrderItem> orderItems = appOrderItemService.listByOrderId(id);
		appProductClient.updateProductSale(BeanUtil.copyToList(orderItems, ProductQuantityDto.class));

		appOrder.setOrderStatus(3);
		appOrder.setReceiveTime(LocalDateTime.now());
		appOrder.setUpdated(LocalDateTime.now());
		this.updateById(appOrder);
	}

	@Override
	public Object pageAdmin(Page page, AppOrder appOrder) {
		return appOrderMapper.pageWithUsername(page, appOrder);
	}

	@Override
	public Object getOrderInfo(Long id) {
		AppOrder appOrder = this.getById(id);
		Assert.notNull(appOrder, "该订单不存在");

		appOrder.setOrderItems(appOrderItemService.listByOrderId(id));

		AppUser appUser = appUserClient.getById(appOrder.getUserId()).getData();
		appOrder.setUsername(appUser.getUsername());
		appOrder.setUserAvatar(appUser.getAvatar());

//		AppRefund appRefund = appRefundMapper.selectOne(new QueryWrapper<AppRefund>().eq("order_id", id));
//		appOrder.setAppRefund(appRefund);

		return appOrder;
	}

	@Override
	public void shipAdmin(AppOrder appOrder) {

		AppOrder order = this.getById(appOrder.getId());
		Assert.notNull(order, "该订单不存在");
		Assert.isTrue(order.getOrderStatus() == 1, "该订单状态不允许发货");
		Assert.isTrue(appOrder.getDeliveryCompany() != null && appOrder.getDeliverySn() != null, "参数异常");

		order.setDeliveryCompany(appOrder.getDeliveryCompany());
		order.setDeliverySn(appOrder.getDeliverySn());
		order.setDeliveryTime(LocalDateTime.now());
		order.setOrderStatus(2);
		order.setUpdated(LocalDateTime.now());

		Long adminId = StpUtil.getLoginIdAsLong();
		order.setAdminNote("管理员：" + adminId + " ：发货；" + order.getAdminNote());

		this.updateById(order);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void closeAdmin(Long id, String adminNote, Long adminId) {

		// 被删除或者状态不是待支付的订单统统跳过即可。

		AppOrder order = this.getById(id);
		if (order == null) {
			log.warn("当前订单被删除了 - orderId: {}", id);
			return;
		}

		Assert.notNull(adminId, "管理员不能为空");
		Assert.notNull(adminNote, "管理员备注不能为空");

		if (order.getOrderStatus() != 0) {
			log.warn("当前订单状态不能取消，订单ID: {}， 订单状态：{}", order.getId(), order.getOrderStatusStr());
			return;
		}

		order.setOrderStatus(4);
		order.setUpdated(LocalDateTime.now());

		order.setAdminNote("管理员：" + adminId + " ：关闭；" + order.getAdminNote());

		this.updateById(order);

		// 释放库存
		List<AppOrderItem> appOrderItems = appOrderItemService.listByOrderId(id);
		appSkuStockClient.releaseStock(BeanUtil.copyToList(appOrderItems, ProductQuantityDto.class));

	}

	private String generateOrderSn() {
		String dateStr = DateUtil.format(new Date(), "yyMMddHHmm");
		return "D" + dateStr + RandomUtil.randomNumbers(4);
	}

	private List<AppCartItem> getCartItemsByOrderDto(long userId, OrderDto orderDto) {

		PreviewDto previewDto = BeanUtil.toBean(orderDto, PreviewDto.class);
		previewDto.setUserId(userId);

		Result<List<AppCartItem>> result = appCartItemClient.getCartItemsByDto(previewDto);
		if (result.getCode() != 200) {
			throw new HubException("获取购物车失败");
		}
		return result.getData();
	}
}




