package com.markerhub.product.feign;

import com.markerhub.core.lang.Result;
import com.markerhub.product.entity.AppProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "appProductClient", name = "shop-product")
public interface AppProductClient {

	@GetMapping("/app/product/{productId}")
	Result<AppProduct> getById(@PathVariable("productId") Long productId);

	@GetMapping("/app/product/onSales")
	Result<List<AppProduct>> listOnSales();
}
