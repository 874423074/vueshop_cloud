package com.markerhub.order.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AlipayConfig {

	@Value("${ali.serverUrl}")
	private String serverUrl;

	@Value("${ali.appId}")
	private String appId;

	@Value("${ali.privateKey}")
	private String privateKey;

	@Value("${ali.alipayPublicKey}")
	private String alipayPublicKey;

	@Value("${ali.notifyUrl}")
	private String notifyUrl;

	@Value("${ali.returnUrl}")
	private String returnUrl;


}
