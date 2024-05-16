package com.markerhub.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.markerhub.system.dto.AdminInfoDto;
import com.markerhub.system.dto.LoginDto;
import com.markerhub.core.lang.Result;
import com.markerhub.system.entity.SysUser;
import com.markerhub.system.service.SysMenuService;
import com.markerhub.system.service.SysUserService;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sys")
public class SysLoginController {

	@Resource
	SysUserService sysUserService;

	@Resource
	SysMenuService sysMenuService;

	@PostMapping("/login")
	public Result login(@Validated @RequestBody LoginDto loginDto) {

		// 校验用户名密码
		SysUser sysUser = sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", loginDto.getUsername()));
		Assert.notNull(sysUser, "用户名或密码错误");

		if (!sysUser.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
			return Result.fail("用户名或密码错误");
		}

		// 生成token
		StpUtil.login(sysUser.getId());

		return Result.success(MapUtil.builder()
				.put("token", StpUtil.getTokenValue())
				.build()
		);
	}

	@GetMapping("/userInfo")
	public Result userInfo() {
		SysUser sysUser = sysUserService.getById(StpUtil.getLoginIdAsLong());

		return Result.success(MapUtil.builder()
				.put("userInfo", BeanUtil.toBean(sysUser, AdminInfoDto.class))
				.put("menus", sysMenuService.getCurrentUserNav())
				.put("auths", sysMenuService.getUserPerms(StpUtil.getLoginIdAsLong()))
				.build()
		);

	}

}
