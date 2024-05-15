package com.markerhub.satoken.service;

import cn.dev33.satoken.stp.StpInterface;

import java.util.Arrays;
import java.util.List;

public class StpInterfaceImpl implements StpInterface {

	@Override
	public List<String> getPermissionList(Object o, String s) {
		return Arrays.asList("user:list");
	}

	@Override
	public List<String> getRoleList(Object o, String s) {
		return null;
	}
}
