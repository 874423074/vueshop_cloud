package com.markerhub.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.product.dto.AppSkuDto;
import com.markerhub.product.dto.ProductQuantityDto;
import com.markerhub.product.entity.AppSkuStock;

import java.util.List;

/**
 *
 */
public interface AppSkuStockService extends IService<AppSkuStock> {

	List<AppSkuStock> listByProductId(Long productId);

	AppSkuStock getSku(Long skuId, Long productId);

	void reduceStock(List<ProductQuantityDto> dtos);

	void releaseStock(List<ProductQuantityDto> dtos);

	Object getSkuByProductId(Long productId);

	void saveSku(AppSkuDto skuDto);
}
