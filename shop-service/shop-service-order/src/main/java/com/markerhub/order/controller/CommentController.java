package com.markerhub.order.controller;

import com.markerhub.core.lang.Result;
import com.markerhub.order.entity.AppComment;
import com.markerhub.satoken.annotation.Login;
import com.markerhub.mybatis.base.BaseController;
import com.markerhub.order.service.AppCommentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app/comment")
public class CommentController extends BaseController {

	@Resource
	AppCommentService appCommentService;

	@GetMapping("/product")
	public Result product(long productId) {
		return Result.success(appCommentService.pageByProductId(getPage(), productId));
	}

	@Login
	@GetMapping("/list")
	public Result list(Integer status) {
		return Result.success(appCommentService.getPage(getPage(), status));
	}

	@Login
	@GetMapping("/info")
	public Result info(Long id) {
		return Result.success(appCommentService.getById(id));
	}

	@Login
	@PostMapping("/post")
	public Result post(MultipartFile[] pics, @Validated AppComment appComment) {
		long id = appCommentService.post(pics, appComment);
		return Result.success(id);
	}


}
