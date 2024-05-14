package com.markerhub.redis.interceptor;

import com.markerhub.core.lang.Result;
import com.markerhub.redis.annatation.NoRepeatSubmit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RepeatSubmitAspect {

	@Resource
	RedissonClient redissonClient;

	@Pointcut("@annotation(noRepeatSubmit)")
	public void pointCut(NoRepeatSubmit noRepeatSubmit) {
	}

	@Around("pointCut(noRepeatSubmit)")
	public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {

		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();

		String token = request.getHeader("token");
		String path = request.getServletPath();

		// 用户身份和请求链接作为key
		String key = token + path;

		RLock rLock = redissonClient.getLock(key);

		boolean isSuccess = rLock.tryLock(0, noRepeatSubmit.lockTime(), TimeUnit.SECONDS);
		if (isSuccess) {
			// 获取锁成功，执行方法
			return pjp.proceed();
		} else {
			return Result.fail("重复请求，请稍后再试");
		}
	}
}
