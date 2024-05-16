package com.markerhub.satoken.service;

import cn.dev33.satoken.stp.StpInterface;
import com.markerhub.system.feign.SysUserClient;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

public class StpInterfaceImpl implements StpInterface {

	@Resource
	SysUserClient sysUserClient;

	@Override
	public List<String> getPermissionList(Object o, String s) {
		return sysUserClient.getUserPerms().getData();
	}

	@Override
	public List<String> getRoleList(Object o, String s) {
		return sysUserClient.getUserRoles().getData();
	}
}
