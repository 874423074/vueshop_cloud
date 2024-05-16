package com.markerhub.system.feign;

import com.markerhub.core.lang.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "shop-system", contextId = "sysUserClient")
public interface SysUserClient {

	@GetMapping("/sys/user/roles")
	Result<List<String>> getUserRoles();

	@GetMapping("/sys/user/perms")
	Result<List<String>> getUserPerms();
}
