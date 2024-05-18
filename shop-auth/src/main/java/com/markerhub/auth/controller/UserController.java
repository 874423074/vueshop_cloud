package com.markerhub.auth.controller;

import cn.hutool.core.bean.BeanUtil;
import com.markerhub.auth.service.AppUserService;
import com.markerhub.core.lang.Result;
import com.markerhub.user.dto.UserInfoDto;
import com.markerhub.user.entity.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/app")
public class UserController {

	@Resource
	AppUserService appUserService;

	//feign
	@GetMapping("/userInfo")
	Result<UserInfoDto> getById(long userId) {
		AppUser appUser = appUserService.getById(userId);
		return Result.success(BeanUtil.toBean(appUser, UserInfoDto.class));
	}

}
