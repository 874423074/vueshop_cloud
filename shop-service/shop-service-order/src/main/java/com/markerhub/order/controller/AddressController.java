package com.markerhub.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.markerhub.core.lang.Result;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.order.entity.AppAddress;
import com.markerhub.order.service.AppAddressService;
import com.markerhub.satoken.annotation.Login;
import com.markerhub.satoken.utils.StpUserUtil;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app/address")
public class AddressController extends BaseController {

	@Resource
	AppAddressService appAddressService;

	@Login
	@GetMapping("/info/{id}")
	public Result info(@PathVariable long id) {
		AppAddress appAddress = appAddressService.getById(id);
		Assert.isTrue(appAddress.getUserId() == StpUserUtil.getLoginIdAsLong(), "收货地址不存在");
		return Result.success(appAddress);
	}

	@Login
	@GetMapping("/list")
	public Result list() {
		return Result.success(appAddressService.list(new QueryWrapper<AppAddress>()
				.eq("user_id", StpUserUtil.getLoginIdAsLong())
				.orderByDesc("created")
		));	}

	@Login
	@PostMapping("/save")
	public Result save(@Validated @RequestBody AppAddress appAddress) {
		appAddressService.saveAndDefault(appAddress);
		return Result.success();
	}

	@Login
	@PostMapping("/delete")
	public Result delete(Long id) {
		appAddressService.remove(new QueryWrapper<AppAddress>()
				.eq("id", id)
				.eq("user_id", StpUserUtil.getLoginIdAsLong())
		);
		return Result.success();
	}
}
