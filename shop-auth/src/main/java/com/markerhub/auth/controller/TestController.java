package com.markerhub.auth.controller;

import com.markerhub.core.lang.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/test")
	public Result test(){
		return Result.success();
	}

}
