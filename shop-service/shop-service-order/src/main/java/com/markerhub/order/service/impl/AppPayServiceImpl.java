package com.markerhub.order.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.markerhub.core.exception.HubException;
import com.markerhub.order.config.AlipayConfig;
import com.markerhub.order.entity.AppOrder;
import com.markerhub.order.service.AppOrderService;
import com.markerhub.order.service.AppPayService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class AppPayServiceImpl implements AppPayService {

	@Resource
	AppOrderService appOrderService;

	@Resource
	AlipayConfig alipayConfig;

	@Override
	public Object pay(String orderSn) {
		AppOrder appOrder = appOrderService.getOwnBySn(orderSn);
		Assert.isTrue(appOrder.getOrderStatus() == 0, "支付状态异常");

		AlipayTradeWapPayResponse response = alipay(appOrder);
		if (!response.isSuccess()) {
			throw new HubException("支付失败，请稍后再试");
		}
		System.out.println(response.getBody());
		return response.getBody();
	}

	@Override
	public Object payCheck(String orderSn) {
		AppOrder appOrder = appOrderService.getOwnBySn(orderSn);
		if (appOrder.getOrderStatus() == 1) {
			// 已经同步支付成功
			return "success";
		}

		AlipayTradeQueryResponse response = aliCheck(orderSn);

		if (response.isSuccess()) {
			String tradeStatus = response.getTradeStatus();
			String tradeNo = response.getTradeNo();

			if ("TRADE_SUCCESS".equals(tradeStatus)) {
				appOrder.setOrderStatus(1);
				appOrder.setPayTradeNo(tradeNo);
				appOrder.setPayTime(LocalDateTime.now());
				appOrderService.updateById(appOrder);

				return "success";
			}
		}
		return response.getSubMsg();
	}

	@Override
	public void refund(Long refundId) {
		// TODO
	}

	private AlipayTradeQueryResponse aliCheck(String orderSn) {
		AlipayClient alipayClient = new DefaultAlipayClient(
				alipayConfig.getServerUrl(),
				alipayConfig.getAppId(),
				alipayConfig.getPrivateKey(),
				"json",
				"UTF-8",
				alipayConfig.getAlipayPublicKey(),
				"RSA2");

		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		JSONObject bizContent = new JSONObject();
		bizContent.put("out_trade_no", orderSn);
		request.setBizContent(bizContent.toString());
		try {
			return alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			throw new HubException("查询异常，请稍后再试");
		}
	}

	private AlipayTradeWapPayResponse alipay(AppOrder appOrder) {

		AlipayClient alipayClient = new DefaultAlipayClient(
				alipayConfig.getServerUrl(),
				alipayConfig.getAppId(),
				alipayConfig.getPrivateKey(),
				"json",
				"UTF-8",
				alipayConfig.getAlipayPublicKey(),
				"RSA2");

		AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
		request.setNotifyUrl(alipayConfig.getNotifyUrl());
		request.setReturnUrl(alipayConfig.getReturnUrl());

		JSONObject bizContent = new JSONObject();
		bizContent.put("out_trade_no", appOrder.getSn());
		bizContent.put("total_amount", appOrder.getTotalAmount());
		bizContent.put("subject", "VueShop商城购买商品");
		bizContent.put("product_code", "QUICK_WAP_WAY");
		bizContent.put("time_expire", DateUtil.formatLocalDateTime(appOrder.getCreated().plusHours(1)));


		request.setBizContent(bizContent.toString());
		AlipayTradeWapPayResponse response = null;
		try {
			response = alipayClient.pageExecute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			throw new HubException("支付异常，请稍后再试");
		}

		return response;
	}
}
