package com.markerhub.order.controller.admin;

import cn.hutool.core.map.MapUtil;
import com.markerhub.core.lang.Result;
import com.markerhub.order.entity.AppRefund;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.order.service.AppOrderService;
import com.markerhub.order.service.AppRefundService;
import com.markerhub.core.uitls.RefundStatusUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/refund")
public class AdminRefundController extends BaseController {

	@Resource
	AppRefundService appRefundService;

	@Resource
	AppOrderService appOrderService;

	@GetMapping("/list")
	public Result list(AppRefund appRefund) {
		return Result.success(MapUtil.builder()
				.put("refundStatusList", RefundStatusUtil.getStatusList())
				.put("pageData", appRefundService.pageAdmin(getPage(), appRefund))
				.build()
		);
	}

	@GetMapping("/info/{id}")
	public Result info(@PathVariable Long id) {
		AppRefund refund = appRefundService.getById(id);
		return Result.success(MapUtil.builder()
				.put("refund", refund)
				.put("order", appOrderService.getOrderInfo(refund.getOrderId()))
				.build()
		);
	}

	@PostMapping("/audit")
	public Result audit(@RequestBody AppRefund appRefund) {
		appRefundService.auditAdmin(appRefund);
		return Result.success();
	}
}
