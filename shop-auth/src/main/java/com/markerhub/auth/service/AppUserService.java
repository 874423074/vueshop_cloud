package com.markerhub.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.user.dto.LoginDto;
import com.markerhub.user.entity.AppUser;

/**
 *
 */
public interface AppUserService extends IService<AppUser> {

	AppUser login(LoginDto loginDto);

	AppUser getCurrentUser();

	long getCurrentUserId();
}
