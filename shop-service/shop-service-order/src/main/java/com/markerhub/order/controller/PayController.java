package com.markerhub.order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.markerhub.core.lang.Result;
import com.markerhub.order.config.AlipayConfig;
import com.markerhub.order.entity.AppOrder;
import com.markerhub.satoken.annotation.Login;
import com.markerhub.redis.annatation.NoRepeatSubmit;
import com.markerhub.order.service.AppOrderService;
import com.markerhub.order.service.AppPayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class PayController {

	@Resource
	AppPayService appPayService;

	@Resource
	AlipayConfig alipayConfig;

	@Resource
	AppOrderService appOrderService;

	@Login
	@NoRepeatSubmit
	@PostMapping("/order/pay")
	public Result pay(String orderSn) {
		return Result.success(appPayService.pay(orderSn));
	}

	@Login
	@PostMapping("/pay/check")
	public Result payCheck(String orderSn) {
		return Result.success(appPayService.payCheck(orderSn));
	}

	@Login
	@PostMapping("/pay/notify")
	public void payNotify(@RequestParam Map<String, String> paramMap, HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

		try {
			boolean flag = AlipaySignature.rsaCheckV1(paramMap, alipayConfig.getAlipayPublicKey(), "GBK","RSA2");

			if (flag) {
				//商户订单号
				String outTradeNo = paramMap.get("out_trade_no");

				//支付宝交易号
				String tradeNo = paramMap.get("trade_no");

				//交易状态
				String tradeStatus = paramMap.get("trade_status");

				if ("TRADE_SUCCESS".equals(tradeStatus)) {

					AppOrder appOrder = appOrderService.getBySn(outTradeNo);
					if (appOrder.getOrderStatus() == 0) {

						appOrder.setOrderStatus(1);
						appOrder.setPayTradeNo(tradeNo);
						appOrder.setPayTime(LocalDateTime.now());
						appOrderService.updateById(appOrder);
						out.print("success");
					}
				}
			} else {
				out.print("fail");
			}


		} catch (AlipayApiException e) {
			e.printStackTrace();
			out.print("fail");
		}

		out.flush();
		out.close();
		out = null;
	}
}
