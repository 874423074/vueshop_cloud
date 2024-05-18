package com.markerhub.product.feign;

import com.markerhub.core.lang.Result;
import com.markerhub.product.dto.ProductQuantityDto;
import com.markerhub.product.entity.AppSkuStock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "shop-product", contextId = "appSkuStockClient")
public interface AppSkuStockClient {

	@GetMapping("/app/product/sku")
	Result<AppSkuStock> getSku(@RequestParam("skuId") long skuId, @RequestParam("productId") long productId);

	@PostMapping("/app/product/reduceStock")
	void reduceStock(List<ProductQuantityDto> dtos);

	@PostMapping("/app/product/releaseStock")
	void releaseStock(List<ProductQuantityDto> dtos);
}
