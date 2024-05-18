package com.markerhub.auth.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.user.dto.LoginDto;
import com.markerhub.core.lang.Const;
import com.markerhub.user.entity.AppUser;
import com.markerhub.auth.mapper.AppUserMapper;
import com.markerhub.auth.service.AppUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 *
 */
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser>
    implements AppUserService{

	@Resource
	HttpSession httpSession;

	@Override
	public AppUser login(LoginDto loginDto) {

		AppUser appUser = this.getOne(new QueryWrapper<AppUser>()
				.eq("username", loginDto.getUsername())
		);

		// 校验用户名和密码
		Assert.notNull(appUser, "用户名或密码错误");
		Assert.isTrue(appUser.getPassword().equals(SecureUtil.md5(loginDto.getPassword())), "用户名或密码错误");

		// 更新最后登录时间
		appUser.setLastLogin(LocalDateTime.now());
		this.updateById(appUser);

		return appUser;
	}

	@Override
	public AppUser getCurrentUser() {
		return this.getById(getCurrentUserId());
	}

	@Override
	public long getCurrentUserId() {
		Long userId = (Long) httpSession.getAttribute(Const.USER_KEY);
		return userId == null ? -1 : userId;
	}
}




