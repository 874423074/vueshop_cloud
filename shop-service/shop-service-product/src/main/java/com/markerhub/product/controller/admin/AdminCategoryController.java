package com.markerhub.product.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.markerhub.core.lang.Result;
import com.markerhub.product.entity.AppCategory;
import com.markerhub.product.entity.AppProduct;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.product.service.AppCategoryService;
import com.markerhub.product.service.AppProductService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController extends BaseController {

	@Resource
	AppCategoryService appCategoryService;

	@Resource
	AppProductService appProductService;

	@GetMapping("/list")
	//@RequiresPermissions("admin:category:list")
	public Result list(String name) {
		return Result.success(appCategoryService.page(getPage(), new QueryWrapper<AppCategory>()
				.like(StrUtil.isNotBlank(name), "name", name)
				.orderByDesc("created")));
	}

	@GetMapping("/info/{id}")
	//@RequiresPermissions("admin:category:list")
	public Result info(@PathVariable Long id) {
		return Result.success(appCategoryService.getById(id));
	}

	@PostMapping("/save")
	//@RequiresPermissions(value = {"admin:category:save", "admin:category:update"}, logical = Logical.OR)
	public Result save(@Validated @RequestBody AppCategory appCategory) {
		if (appCategory.getId() == null) {
			appCategory.setCreated(LocalDateTime.now());
		} else {
			appCategory.setUpdated(LocalDateTime.now());
		}

		appCategoryService.saveOrUpdate(appCategory);
		return Result.success(appCategory);
	}

	@PostMapping("/delete")
	//@RequiresPermissions("admin:category:delete")
	public Result delete(@RequestBody Long[] ids) {

		long count = appProductService.count(new QueryWrapper<AppProduct>().in("category_id", Arrays.asList(ids)));
		if (count > 0) {
			return Result.fail("该分类下有关联商品，不允许删除");
		}

		appCategoryService.removeByIds(Arrays.asList(ids));
		return Result.success();
	}

}
