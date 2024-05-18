package com.markerhub.product.controller;

import com.markerhub.core.lang.Result;
import com.markerhub.product.service.HomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app")
public class HomeController {

	@Resource
	HomeService homeService;

	@GetMapping("/home")
	public Result home() {
		return Result.success(homeService.getContents());
	}
}
