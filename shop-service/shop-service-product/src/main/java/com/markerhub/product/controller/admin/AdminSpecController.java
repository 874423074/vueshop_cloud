package com.markerhub.product.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.markerhub.core.lang.Result;
import com.markerhub.product.entity.AppSpecification;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.product.service.AppSpecificationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/admin/specification")
public class AdminSpecController extends BaseController {

	@Resource
	AppSpecificationService appSpecificationService;

	@GetMapping("/list")
	//@RequiresPermissions("admin:specification:list")
	public Result list(String name) {
		return Result.success(appSpecificationService.page(getPage(), new QueryWrapper<AppSpecification>()
				.like(StrUtil.isNotBlank(name), "name", name)
				.orderByDesc("sort_order", "created")));
	}

	@GetMapping("/info/{id}")
	//@RequiresPermissions("admin:specification:list")
	public Result info(@PathVariable Long id) {
		AppSpecification specification = appSpecificationService.getById(id);
		if (specification.getOptions() == null) {
			specification.setOptions("");
		}
		return Result.success(specification);
	}

	@PostMapping("/save")
	//@RequiresPermissions(value = {"admin:specification:save", "admin:specification:update"}, logical = Logical.OR)
	public Result save(@Validated @RequestBody AppSpecification appSpecification) {
		if (appSpecification.getId() == null) {
			appSpecification.setCreated(LocalDateTime.now());
		} else {
			appSpecification.setUpdated(LocalDateTime.now());
		}

		appSpecificationService.saveOrUpdate(appSpecification);
		return Result.success(appSpecification);
	}

	@PostMapping("/delete")
	//@RequiresPermissions("admin:specification:delete")
	public Result delete(@RequestBody Long[] ids) {
		appSpecificationService.removeByIds(Arrays.asList(ids));
		return Result.success();
	}

}
