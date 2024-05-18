package com.markerhub.product.controller;

import com.markerhub.core.lang.Result;
import com.markerhub.product.entity.AppProduct;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.product.service.AppProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app")
public class ProductController extends BaseController {

	@Resource
	AppProductService appProductService;

	@GetMapping("/category/{categoryId}")
	public Result category(@PathVariable Long categoryId, @RequestParam(defaultValue = "0") Integer sort) {
		return Result.success(appProductService.pageByCategoryId(getPage(), categoryId, sort));
	}

	@GetMapping("/product/{productId}")
	public Result product(@PathVariable Long productId) {
		AppProduct product = appProductService.getWithAttrsById(productId);
		if (!product.getIsOnSale()) {
			return Result.fail("商品已下架");
		}
		return Result.success(product);
	}
}
