package com.markerhub.satoken.interceptor;

import com.markerhub.core.exception.HubException;
import com.markerhub.core.lang.Const;
import com.markerhub.satoken.annotation.InnerAuth;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 内部熊接口安全拦截器
 */
@Component
public class InnerAuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		InnerAuth annotation;
		if (handler instanceof HandlerMethod) {
			annotation = ((HandlerMethod) handler).getMethodAnnotation(InnerAuth.class);
		} else {
			return true;

		}

		if (annotation == null) {
			return true;
		}

		String source = request.getHeader(Const.SA_SOURCE_KEY);
		if (!Const.SA_SOURCE_INNER.equals(source)) {
			throw new HubException("内部系统不能访问");
		}

		return true;

	}
}
