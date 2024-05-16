package com.markerhub.satoken.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.json.JSONUtil;
import com.markerhub.core.lang.Result;
import com.markerhub.satoken.interceptor.InnerAuthInterceptor;
import com.markerhub.satoken.service.StpInterfaceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
public class SatokenConfigure implements WebMvcConfigurer {

	@Bean
	@Primary
	public SaTokenConfig getSaTokenConfigPrimary() {
		SaTokenConfig config = new SaTokenConfig();
		config.setTokenName("satoken");             // token 名称（同时也是 cookie 名称）
		config.setTimeout(30 * 24 * 60 * 60);       // token 有效期（单位：秒），默认30天，-1代表永不过期
		config.setActiveTimeout(-1);              // token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认-1 代表不限制，永不冻结
		config.setIsConcurrent(true);               // 是否允许同一账号多地同时登录（为 true 时允许一起登录，为 false 时新登录挤掉旧登录）
		config.setIsShare(true);                    // 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token，为 false 时每次登录新建一个 token）
		config.setTokenStyle("uuid");               // token 风格
		config.setIsLog(false);                     // 是否输出操作日志

		config.setIsReadCookie(false);
		return config;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注册 Sa-Token 拦截器，打开注解式鉴权功能
		registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(new InnerAuthInterceptor()).addPathPatterns("/**");
	}

	@Bean
	StpInterfaceImpl stpInterface() {
		return new StpInterfaceImpl();
	}

	@PostConstruct
	public void rewriteSaStrategy() {
		// 重写Sa-Token的注解处理器，增加注解合并功能
		SaStrategy.instance.getAnnotation = AnnotatedElementUtils::getMergedAnnotation;
	}

	// 内部服务外网隔离过滤器
	@Bean
	public SaServletFilter getSaServletFilter() {
		return new SaServletFilter()
				.addInclude("/**")
				.addExclude("/favicon.ico")
				.setAuth(obj -> {
					SaSameUtil.checkCurrentRequestToken();
				})
				.setError(e -> {
					SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
					return JSONUtil.toJsonStr(Result.fail("认证失败，无法访问内部系统"));
				});
	}
}
