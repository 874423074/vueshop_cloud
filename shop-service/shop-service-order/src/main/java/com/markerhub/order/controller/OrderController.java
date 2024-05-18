package com.markerhub.order.controller;

import cn.hutool.core.map.MapUtil;
import com.markerhub.order.dto.OrderDto;
import com.markerhub.core.lang.Result;
import com.markerhub.order.entity.AppOrder;
import com.markerhub.satoken.annotation.Login;
import com.markerhub.redis.annatation.NoRepeatSubmit;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.order.service.AppOrderService;
import com.markerhub.satoken.utils.StpUserUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app/order")
public class OrderController extends BaseController {

	@Resource
	AppOrderService appOrderService;

	@Login
	@PostMapping("/preview")
	public Result preview(@RequestBody OrderDto orderDto) {
		return Result.success(appOrderService.preview(orderDto));
	}

	@Login
	@NoRepeatSubmit
	@PostMapping("/create")
	public Result create(@RequestBody OrderDto orderDto) {
		return Result.success(appOrderService.create(orderDto));
	}

	@Login
	@GetMapping("/count")
	public Result count() {
		return Result.success(appOrderService.getOrderCount());
	}

	@Login
	@GetMapping("/list")
	public Result list(Integer status) {
		long userId = StpUserUtil.getLoginIdAsLong();
		return Result.success(appOrderService.getPage(getPage(), userId, status));
	}

	@Login
	@GetMapping("/detail/{id}")
	public Result detail(@PathVariable Long id) {
		return Result.success(appOrderService.detail(id));
	}

	@Login
	@PostMapping("/cancel")
	public Result cancel(Long id) {
		appOrderService.cancel(id);
		return Result.success();
	}

	@Login
	@PostMapping("/delete")
	public Result delete(Long id) {
		appOrderService.delete(id);
		return Result.success();
	}

	@Login
	@GetMapping("/deliveryInfo")
	public Result deliveryInfo(Long id) throws Exception {
		AppOrder appOrder = appOrderService.getOwnById(id);
		return Result.success(
				MapUtil.builder()
						.put("deliveryInfo", appOrderService.getDeliveryInfo(appOrder))
						.put("deliveryCompany", appOrder.getDeliveryCompany())
						.put("deliverySn", appOrder.getDeliverySn())
						.build()
		);
	}

	@Login
	@PostMapping("/confirm")
	public Result confirm(Long id) {
		appOrderService.confirm(id);
		return Result.success();
	}
}
