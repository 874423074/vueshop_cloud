package com.markerhub.product.controller.admin;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.markerhub.core.lang.Result;
import com.markerhub.product.entity.AppProduct;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.product.service.AppCategoryService;
import com.markerhub.product.service.AppProductService;
import com.markerhub.rabbit.config.SearchRabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController extends BaseController {

	@Resource
	AppProductService appProductService;

	@Resource
	AppCategoryService appCategoryService;

	@Resource
	RabbitTemplate rabbitTemplate;

	@GetMapping("/list")
	//@RequiresPermissions("admin:product:list")
	public Result list(String name, Long categoryId) {
		return Result.success(MapUtil.builder()
				.put("categories", appCategoryService.list())
				.put("pageData", appProductService.pageWithCategory(getPage(), name, categoryId))
				.build()
		);
	}

	@GetMapping("/info/{id}")
	//@RequiresPermissions("admin:product:list")
	public Result info(@PathVariable Long id) {
		return Result.success(appProductService.getWithAttrsById(id));
	}

	@PostMapping("/save")
	//@RequiresPermissions(value = {"admin:product:save", "admin:product:update"}, logical = Logical.OR)
	public Result save(@Validated @RequestBody AppProduct appProduct) {

		appProductService.create(appProduct);

		// 同步es与mysql的商品数据
		rabbitTemplate.convertAndSend(SearchRabbitConfig.SEARCH_EXCHANGE, SearchRabbitConfig.PRODUCT_SEARCH_CREATE_KEY, appProduct.getId());
		return Result.success(appProduct);
	}

	@PostMapping("/delete")
	//@RequiresPermissions("admin:product:delete")
	public Result delete(@RequestBody Long[] ids) {

		appCategoryService.removeByIds(Arrays.asList(ids));

		rabbitTemplate.convertAndSend(SearchRabbitConfig.SEARCH_EXCHANGE, SearchRabbitConfig.PRODUCT_SEARCH_DELETE_KEY, Arrays.asList(ids));
		return Result.success();
	}

	@PostMapping("/updateState")
	//@RequiresPermissions("admin:product:updateState")
	public Result updateState(long id, Boolean isOnSale, Boolean isNew, Boolean isTop, Boolean isHot) {

		appProductService.update(new UpdateWrapper<AppProduct>()
				.set(isOnSale != null, "is_on_sale", isOnSale)
				.set(isNew != null, "is_new", isNew)
				.set(isTop != null, "is_top", isTop)
				.set(isHot != null, "is_hot", isHot)
				.eq("id", id)
		);

		if (isOnSale != null) {
			rabbitTemplate.convertAndSend(SearchRabbitConfig.SEARCH_EXCHANGE, SearchRabbitConfig.PRODUCT_SEARCH_CREATE_KEY, id);
		}

		return Result.success();
	}

}
