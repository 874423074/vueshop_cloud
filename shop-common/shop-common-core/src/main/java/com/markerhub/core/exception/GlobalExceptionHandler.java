package com.markerhub.core.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.markerhub.core.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = HubException.class)
	public Result handler(HubException e) {
		log.error("自定义异常 ---- {}", e.getMessage());
		return Result.fail(e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = RuntimeException.class)
	public Result handler(RuntimeException e) {
		log.error("运行时异常 ---- {}", e.getMessage());
		e.printStackTrace();
		return Result.fail(e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = BindException.class)
	public Result handler(BindException e) {
		BindingResult bindingResult = e.getBindingResult();
		ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();

		log.error("实体校验异常 ---- {}", objectError.getDefaultMessage());
		return Result.fail(objectError.getDefaultMessage());
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = NotLoginException.class)
	public Result handler(NotLoginException e) {
		Result<Object> result = Result.fail("请先登录");
		result.setCode(401);
		return result;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(value = NotPermissionException.class)
	public Result handler(NotPermissionException e) {
		return Result.fail("无权限操作");
	}


}
