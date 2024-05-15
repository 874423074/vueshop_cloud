package com.markerhub.satoken.annotation;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.markerhub.satoken.utils.StpUserUtil;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SaCheckLogin(type = StpUserUtil.TYPE)
public @interface Login {
}
