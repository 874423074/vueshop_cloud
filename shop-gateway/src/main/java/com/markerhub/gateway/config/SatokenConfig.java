package com.markerhub.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.markerhub.core.lang.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SatokenConfig {

	/**
	 * 后台的链接需要登录后才能访问
	 *
	 * @return
	 */
	@Bean
	public SaReactorFilter saReactorFilter() {
		return new SaReactorFilter()
				.addInclude("/**")
				.addExclude("/favicon.ico", "/*/app/**")
				.setAuth(obj -> {
					SaRouter.match("/*/admin/**", r -> StpUtil.checkLogin());
					SaRouter.match("/*/sys/**", "/*/sys/login", r -> StpUtil.checkLogin());
				})
				.setError(e -> {
					SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
					return JSONUtil.toJsonStr(Result.fail("请先登录"));
				})
				// 前置函数：在每次认证函数之前执行
				.setBeforeAuth(obj -> {
					SaHolder.getResponse()
							.setHeader("Access-Control-Allow-Origin", "*")
							.setHeader("Access-Control-Allow-Methods", "*")
							.setHeader("Access-Control-Allow-Headers", "*")
							.setHeader("Access-Control-Max-Age", "3600");

					// 如果是预检请求，则立即返回到前端
					SaRouter.match(SaHttpMethod.OPTIONS).back();
				});
	}

}
