package com.markerhub.product.controller.admin;

import com.markerhub.product.dto.AppSkuDto;
import com.markerhub.core.lang.Result;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.product.service.AppSkuStockService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/sku")
public class AdminSkuController extends BaseController {

	@Resource
	AppSkuStockService appSkuStockService;

	@GetMapping("/product/{productId}")
	public Result sku(@PathVariable Long productId) {
		return Result.success(appSkuStockService.getSkuByProductId(productId));
	}

	@PostMapping("/save")
	public Result save(@Validated @RequestBody AppSkuDto skuDto) {

		appSkuStockService.saveSku(skuDto);
		return Result.success();
	}

}
