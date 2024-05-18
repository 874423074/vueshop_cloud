package com.markerhub.user.feign;

import com.markerhub.core.lang.Result;
import com.markerhub.user.entity.AppUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "shop-auth", contextId = "appUserClient")
public interface AppUserClient {

	@GetMapping("/app/userInfo")
	Result<AppUser> getById(@RequestParam("userId") long userId);
}
