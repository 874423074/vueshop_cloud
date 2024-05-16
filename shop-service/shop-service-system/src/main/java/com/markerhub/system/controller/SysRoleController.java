package com.markerhub.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.core.lang.Result;
import com.markerhub.system.entity.SysRole;
import com.markerhub.system.entity.SysRoleMenu;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.system.service.SysRoleMenuService;
import com.markerhub.system.service.SysRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {

	@Resource
	SysRoleService sysRoleService;

	@Resource
	SysRoleMenuService sysRoleMenuService;

	@GetMapping("/list")
	@SaCheckPermission("sys:role:list")
	public Result list(SysRole sysRole) {
		Page pageData = sysRoleService.page(getPage(), new QueryWrapper<SysRole>()
				.like(StrUtil.isNotBlank(sysRole.getName()), "name", sysRole.getName())
				.like(StrUtil.isNotBlank(sysRole.getCode()), "code", sysRole.getCode())
				.orderByDesc("created"));

		return Result.success(pageData);
	}

	@GetMapping("/info/{id}")
	@SaCheckPermission("sys:role:list")
	public Result info(@PathVariable Long id) {
		SysRole sysRole = sysRoleService.getById(id);

		List<Long> menuIds = sysRoleMenuService.listObjs(new QueryWrapper<SysRoleMenu>()
				.eq("role_id", id).select("menu_id"),
				o -> Long.parseLong(o.toString()));

		sysRole.setMenuIds(menuIds);
		return Result.success(sysRole);
	}

	@PostMapping("/save")
	@SaCheckPermission(value = {"sys:role:save", "sys:role:update"}, mode = SaMode.OR)
	public Result save(@Validated @RequestBody SysRole sysRole) {
		if (sysRole.getId() == null) {
			sysRole.setCreated(LocalDateTime.now());
		} else {
			sysRole.setUpdated(LocalDateTime.now());
		}

		sysRoleService.saveOrUpdate(sysRole);
		return Result.success(sysRole);
	}

	@PostMapping("/delete")
	@SaCheckPermission("sys:role:delete")
	public Result delete(@RequestBody Long[] ids) {
		sysRoleService.deleteBath(ids);
		return Result.success();
	}

	@PostMapping("/perm/{roleId}")
	@SaCheckPermission("sys:role:perm")
	public Result perm(@PathVariable Long roleId, @RequestBody Long[] menuIds) {
		sysRoleService.perm(roleId, menuIds);
		return Result.success();
	}

}
