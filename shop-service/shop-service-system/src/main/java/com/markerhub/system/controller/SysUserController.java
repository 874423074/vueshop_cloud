package com.markerhub.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.markerhub.core.lang.Result;
import com.markerhub.satoken.annotation.InnerAuth;
import com.markerhub.system.entity.SysMenu;
import com.markerhub.system.entity.SysRole;
import com.markerhub.system.entity.SysUser;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.system.service.SysMenuService;
import com.markerhub.system.service.SysRoleService;
import com.markerhub.system.service.SysUserService;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

	@Resource
	SysUserService sysUserService;

	@Resource
	SysRoleService sysRoleService;

	@Resource
	SysMenuService sysMenuService;

	@GetMapping("/list")
	@SaCheckPermission("sys:user:list")
	public Result list(SysUser sysUser) {
		return Result.success(sysUserService.getPage(getPage(), sysUser));
	}

	@GetMapping("/info/{id}")
	@SaCheckPermission("sys:user:list")
	public Result info(@PathVariable Long id) {
		SysUser sysUser = sysUserService.getById(id);
		Assert.notNull(sysUser, "该管理员不存在");

		sysUser.setRoles(sysRoleService.listRolesByUserId(id));
		return Result.success(sysUser);
	}

	@PostMapping("/save")
	@SaCheckPermission(value = {"sys:user:save", "sys:user:update"}, mode = SaMode.OR)
	public Result save(@Validated @RequestBody SysUser sysUser) {
		if (sysUser.getId() == null) {
			sysUser.setCreated(LocalDateTime.now());
			sysUser.setAvatar("https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/zhuawaba/5a9f48118166308daba8b6da7e466aab.jpg");
		} else {
			sysUser.setUpdated(LocalDateTime.now());

			SysUser temp = sysUserService.getById(sysUser.getId());
			BeanUtil.copyProperties(sysUser, temp, "password", "created", "updated");

			// 密码不为空时候更新密码
			if (StrUtil.isNotBlank(sysUser.getPassword())) {
				temp.setPassword(SecureUtil.md5(sysUser.getPassword()));
			}
		}

		sysUserService.saveOrUpdate(sysUser);
		return Result.success(sysUser);
	}

	@PostMapping("/delete")
	@SaCheckPermission("sys:user:delete")
	public Result delete(Long[] ids) {
		sysUserService.deleteBath(ids);
		return Result.success();
	}

	@PostMapping("/role/{userId}")
	@SaCheckPermission("sys:user:role")
	public Result role(@PathVariable Long userId, @RequestBody Long[] roleIds) {
		sysUserService.role(userId, roleIds);
		return Result.success();
	}

	@PostMapping("/resetPwd")
	@SaCheckPermission("sys:user:resetPwd")
	public Result resetPwd(Long userId) {
		SysUser sysUser = sysUserService.getById(userId);
		sysUser.setPassword(SecureUtil.md5("888888"));
		sysUser.setUpdated(LocalDateTime.now());
		sysUserService.updateById(sysUser);
		return Result.success();
	}

	// feign
	@InnerAuth
	@GetMapping("/roles")
	public Result<List<String>> getUserRoles() {
		List<SysRole> sysRoles = sysRoleService.listRolesByUserId(StpUtil.getLoginIdAsLong());

		return Result.success(
				sysRoles.stream().map(SysRole::getCode).collect(Collectors.toList())
		);
	}

	@InnerAuth
	@GetMapping("/perms")
	public Result<List<String>> getUserPerms() {
		List<SysMenu> sysMenus = sysMenuService.listMenusByUserId(StpUtil.getLoginIdAsLong());

		return Result.success(
				sysMenus.stream().map(SysMenu::getPerms).collect(Collectors.toList())
		);
	}

}
