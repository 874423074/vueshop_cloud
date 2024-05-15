package com.markerhub.auth.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.markerhub.core.lang.Result;
import com.markerhub.satoken.annotation.Login;
import com.markerhub.satoken.utils.StpUserUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/test")
	public Result test(){
		return Result.success();
	}

	@GetMapping("/login")
	public Result login(){
		StpUtil.login(1000);
		String tokenValue = StpUtil.getTokenValue();
		System.out.println(tokenValue);
		System.out.println(StpUtil.getTokenName());
		return Result.success(StpUtil.getLoginId());
	}
	@GetMapping("/userLogin")
	public Result userLogin(){
		StpUserUtil.login(1000);
		String tokenValue = StpUserUtil.getTokenValue();
		System.out.println(tokenValue);
		System.out.println(StpUserUtil.getTokenName());
		return Result.success(StpUserUtil.getLoginId());
	}

	@GetMapping("/logout")
	public Result logout(){
		StpUtil.logout();
		return Result.success();
	}

	@GetMapping("/isLogin")
	public Result isLogin(){
		return Result.success(StpUtil.isLogin());
	}

	@SaCheckLogin
	@GetMapping("/checkLogin")
	public Result checkLogin(){
		return Result.success(StpUtil.isLogin());
	}

	@SaCheckPermission("user:list")
	@GetMapping("/checkPerm")
	public Result checkPerm(){
		return Result.success(StpUtil.isLogin());
	}

	@SaCheckLogin(type = StpUserUtil.TYPE)
	@GetMapping("/checkUserLogin")
	public Result checkUserLogin(){
		return Result.success(StpUserUtil.isLogin());
	}

	@Login
	@GetMapping("/checkUserLogin2")
	public Result checkUserLogin2(){
		return Result.success(StpUserUtil.isLogin());
	}




}
