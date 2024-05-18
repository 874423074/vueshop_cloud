package com.markerhub.cartItem.controller;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.markerhub.cartItem.dto.PreviewDto;
import com.markerhub.core.lang.Result;
import com.markerhub.cartItem.entity.AppCartItem;
import com.markerhub.satoken.annotation.InnerAuth;
import com.markerhub.satoken.annotation.Login;
import com.markerhub.cartItem.service.AppCartItemService;
import com.markerhub.satoken.utils.StpUserUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/app/cart")
public class CartItemController {

	@Resource
	AppCartItemService appCartItemService;

	@Login
	@GetMapping("/list")
	public Result getCurrent() {
		return Result.success(appCartItemService.getCurrent());
	}

	@Login
	@GetMapping("/count")
	public Result count() {
		return Result.success(appCartItemService.getCount());
	}

	@Login
	@PostMapping("/total")
	public Result total(@RequestBody Long[] ids) {
		return Result.success(appCartItemService.getTotal(ListUtil.toList(ids)));
	}

	@Login
	@PostMapping("/updateQuantity")
	public Result updateQuantity(Long id, Integer quantity) {
		appCartItemService.updateQuantity(id, quantity);
		return Result.success();
	}

	@Login
	@PostMapping("/add")
	public Result add(@Validated @RequestBody AppCartItem appCartItem) {
		appCartItemService.add(appCartItem);
		return Result.success();
	}

	@Login
	@PostMapping("/delete")
	public Result delete(@RequestBody Long[] ids) {
		long userId = StpUserUtil.getLoginIdAsLong();
		appCartItemService.remove(new QueryWrapper<AppCartItem>().eq("user_id", userId).in("id", ListUtil.toList(ids)));
		return Result.success();
	}

	// feign
	@InnerAuth
	@GetMapping("/getCartItemsByDto")
	public Result<List<AppCartItem>> getCartItemsByDto(PreviewDto dto) {
		return Result.success(appCartItemService.getCartItemsByOrderDto(dto));
	}

}
