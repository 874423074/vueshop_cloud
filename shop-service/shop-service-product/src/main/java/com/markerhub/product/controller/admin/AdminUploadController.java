package com.markerhub.product.controller.admin;


import com.markerhub.core.lang.Result;
import com.markerhub.upload.service.AppUploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUploadController {

	@Resource
	AppUploadService appUploadService;

	@PostMapping("/upload")
	public Result upload(MultipartFile[] pics) throws IOException {
		List<String> picPaths = appUploadService.upload(pics, 0.5F, 0.2);
		return Result.success(picPaths);
	}

}
