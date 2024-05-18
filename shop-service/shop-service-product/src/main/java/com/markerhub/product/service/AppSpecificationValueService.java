package com.markerhub.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.product.entity.AppSpecificationValue;

import java.util.List;

/**
 *
 */
public interface AppSpecificationValueService extends IService<AppSpecificationValue> {

	List<AppSpecificationValue> listByProductId(Long productId);
}
