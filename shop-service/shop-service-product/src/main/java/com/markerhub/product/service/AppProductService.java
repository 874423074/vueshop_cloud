package com.markerhub.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.product.entity.AppProduct;

/**
 *
 */
public interface AppProductService extends IService<AppProduct> {

	Object pageByCategoryId(Page page, Long categoryId, Integer sort);

	AppProduct getWithAttrsById(Long productId);

	Page<AppProduct> pageWithCategory(Page page, String name, Long categoryId);

	void create(AppProduct appProduct);
}
