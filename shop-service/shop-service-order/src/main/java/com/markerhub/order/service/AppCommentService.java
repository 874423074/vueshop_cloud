package com.markerhub.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.order.entity.AppComment;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 */
public interface AppCommentService extends IService<AppComment> {

	Object pageByProductId(Page page, long productId);

	Object getPage(Page page, Integer status);

	long post(MultipartFile[] pics, AppComment appComment);
}
