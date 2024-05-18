package com.markerhub.feign.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableFeignClients(basePackages = "com.markerhub.*.feign")
public @interface EnableShopFeignClients {
}
