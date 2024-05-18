package com.markerhub.order.controller;

import com.markerhub.core.lang.Result;
import com.markerhub.order.entity.AppRefund;
import com.markerhub.satoken.annotation.Login;
import com.markerhub.order.service.AppRefundService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app/refund")
public class RefundController {

	@Resource
	AppRefundService appRefundService;

	@Login
	@GetMapping("/order")
	public Result getOrderData(long orderId) {
		return Result.success(appRefundService.getOrderData(orderId));
	}

	@Login
	@PostMapping("/apply")
	public Result apply(MultipartFile[] pics, @Validated AppRefund appRefund) {
		appRefundService.apply(appRefund, pics);
		return Result.success();
	}


}
