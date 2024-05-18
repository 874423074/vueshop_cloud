package com.markerhub.product.feign;

import com.markerhub.core.lang.Result;
import com.markerhub.product.entity.AppSkuStock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "shop-product", contextId = "appSkuStockClient")
public interface AppSkuStockClient {

	@GetMapping("/app/product/sku")
	Result<AppSkuStock> getSku(@RequestParam("skuId") long skuId, @RequestParam("productId") long productId);

}
