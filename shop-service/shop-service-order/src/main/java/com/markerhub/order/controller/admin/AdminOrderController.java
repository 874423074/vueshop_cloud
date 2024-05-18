package com.markerhub.order.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import com.markerhub.core.lang.Result;
import com.markerhub.order.entity.AppOrder;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.order.service.AppOrderService;
import com.markerhub.core.uitls.OrderStatusUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/order")
public class AdminOrderController extends BaseController {

	@Resource
	AppOrderService appOrderService;

	@GetMapping("/list")
	public Result list(AppOrder appOrder) {
		return Result.success(MapUtil.builder()
				.put("orderStatusList", OrderStatusUtil.getStatusList())
				.put("pageData", appOrderService.pageAdmin(getPage(), appOrder))
				.build()
		);
	}

	@GetMapping("/info/{id}")
	public Result info(@PathVariable Long id) {
		return Result.success(appOrderService.getOrderInfo(id));
	}

	@PostMapping("/ship")
	public Result ship(@RequestBody AppOrder appOrder) {
		appOrderService.shipAdmin(appOrder);
		return Result.success();
	}

	@GetMapping("/deliveryInfo")
	public Result deliveryInfo(Long id) throws Exception {
		AppOrder order = appOrderService.getById(id);
		return Result.success(appOrderService.getDeliveryInfo(order));
	}

	@PostMapping("/close")
	public Result close(@RequestBody AppOrder appOrder) {
		Long adminId = StpUtil.getLoginIdAsLong();
		appOrderService.closeAdmin(appOrder.getId(), appOrder.getAdminNote(), adminId);
		return Result.success();
	}

}
