package com.markerhub.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.markerhub.core.lang.Result;
import com.markerhub.system.entity.SysMenu;
import com.markerhub.system.service.SysMenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

	@Resource
	SysMenuService sysMenuService;

	@GetMapping("/list")
	@SaCheckPermission("sys:menu:list")
	public Result list() {
		return Result.success(sysMenuService.tree());
	}

	@GetMapping("/info/{id}")
	@SaCheckPermission("sys:menu:list")
	public Result info(@PathVariable Long id) {
		return Result.success(sysMenuService.getById(id));
	}

	@PostMapping("/save")
	@SaCheckPermission(value = {"sys:menu:save", "sys:menu:update"}, mode = SaMode.OR)
	public Result save(@Validated @RequestBody SysMenu sysMenu) {
		if (sysMenu.getId() == null) {
			sysMenu.setCreated(LocalDateTime.now());
		} else {
			sysMenu.setUpdated(LocalDateTime.now());
		}

		sysMenuService.saveOrUpdate(sysMenu);
		return Result.success(sysMenu);
	}

	@PostMapping("/delete")
	@SaCheckPermission("sys:menu:delete")
	public Result delete(Long id) {
		sysMenuService.delete(id);
		return Result.success();
	}

}
