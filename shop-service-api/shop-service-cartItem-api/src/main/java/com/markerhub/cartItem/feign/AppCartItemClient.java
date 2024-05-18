package com.markerhub.cartItem.feign;

import com.markerhub.cartItem.entity.AppCartItem;
import com.markerhub.cartItem.dto.PreviewDto;
import com.markerhub.core.lang.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "shop-cartItem", contextId = "appCartItemClient")
public interface AppCartItemClient {

	@PostMapping("/app/cart/delete")
	Result<?> delete(@RequestBody List<Long> ids);

	@GetMapping("/getCartItemsByDto")
	Result<List<AppCartItem>> getCartItemsByDto(PreviewDto dto);
}
