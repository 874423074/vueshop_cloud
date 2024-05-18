package com.markerhub.cartItem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.cartItem.entity.AppCartItem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public interface AppCartItemService extends IService<AppCartItem> {

	List<AppCartItem> getCurrent();

	long getCount();

	Object getTotal(ArrayList<Long> ids);

	void updateQuantity(Long cartItemId, Integer quantity);

	void add(AppCartItem appCartItem);

	AppCartItem getCombinedCartItem(long userId, Long productId, Long skuId, Integer quantity);
}
