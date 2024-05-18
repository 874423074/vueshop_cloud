package com.markerhub.product.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.markerhub.core.lang.Result;
import com.markerhub.product.entity.AppAd;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.product.service.AppAdService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/admin/ad")
public class AdminAdController extends BaseController {

	@Resource
	AppAdService appAdService;

	@GetMapping("/list")
	//@RequiresPermissions("admin:ad:list")
	public Result list(String name) {
		return Result.success(appAdService.page(getPage(), new QueryWrapper<AppAd>()
				.like(StrUtil.isNotBlank(name), "name", name)
				.orderByDesc("created")));
	}

	@GetMapping("/info/{id}")
	//@RequiresPermissions("admin:ad:list")
	public Result info(@PathVariable Long id) {
		return Result.success(appAdService.getById(id));
	}

	@PostMapping("/save")
	//@RequiresPermissions(value = {"admin:ad:save", "admin:ad:update"}, logical = Logical.OR)
	public Result save(@Validated @RequestBody AppAd appAd) {
		if (appAd.getId() == null) {
			appAd.setCreated(LocalDateTime.now());
		} else {
			appAd.setUpdated(LocalDateTime.now());
		}

		appAdService.saveOrUpdate(appAd);
		return Result.success(appAd);
	}

	@PostMapping("/delete")
	//@RequiresPermissions("admin:ad:delete")
	public Result delete(@RequestBody Long[] ids) {
		appAdService.removeByIds(Arrays.asList(ids));
		return Result.success();
	}

}
