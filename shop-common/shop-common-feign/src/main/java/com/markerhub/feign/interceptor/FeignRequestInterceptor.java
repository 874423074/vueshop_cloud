package com.markerhub.feign.interceptor;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.same.SaSameUtil;
import com.markerhub.core.lang.Const;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * 发起feign请求之前的拦截器
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {

		if (SaHolder.getContext().isValid()) {

			SaRequest request = SaHolder.getRequest();

			// 用户身份信息传递
			requestTemplate.header(Const.USER_TOKEN_NAME, request.getHeader(Const.USER_TOKEN_NAME, null));
			requestTemplate.header(Const.ADMIN_TOKEN_NAME, request.getHeader(Const.ADMIN_TOKEN_NAME, null));
		}

		requestTemplate.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());
		requestTemplate.header(Const.SA_SOURCE_KEY, Const.SA_SOURCE_INNER);
	}

}
