package com.markerhub.product.feign;

import com.markerhub.core.lang.Result;
import com.markerhub.product.dto.ProductQuantityDto;
import com.markerhub.product.entity.AppProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(contextId = "appProductClient", name = "shop-product")
public interface AppProductClient {

	@GetMapping("/app/product/{productId}")
	Result<AppProduct> getById(@PathVariable("productId") Long productId);

	@GetMapping("/app/product/onSales")
	Result<List<AppProduct>> listOnSales();

	@PostMapping("/app/product/increaseCommentCount")
	void increaseCommentCount(@NotNull Long productId);

	@PostMapping("/app/product/updateSales")
	void updateProductSale(@RequestBody List<ProductQuantityDto> dtos);
}
