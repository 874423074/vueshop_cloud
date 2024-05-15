package com.markerhub.core.exception;

import com.markerhub.core.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

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



}
