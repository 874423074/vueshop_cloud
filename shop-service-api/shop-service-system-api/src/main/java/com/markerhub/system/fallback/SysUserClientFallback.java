package com.markerhub.system.fallback;

import cn.hutool.core.collection.ListUtil;
import com.markerhub.core.lang.Result;
import com.markerhub.system.feign.SysUserClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SysUserClientFallback implements SysUserClient {

	@Override
	public Result<List<String>> getUserRoles() {
		return Result.fail("无法获取角色信息", ListUtil.empty());
	}

	@Override
	public Result<List<String>> getUserPerms() {
		return Result.fail("无法获取权限信息", ListUtil.empty());
	}
}
