package com.markerhub.cartItem.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.cartItem.entity.AppCartItem;
import com.markerhub.product.entity.AppProduct;
import com.markerhub.product.entity.AppSkuStock;
import com.markerhub.cartItem.mapper.AppCartItemMapper;
import com.markerhub.cartItem.service.AppCartItemService;
import com.markerhub.product.feign.AppProductClient;
import com.markerhub.product.feign.AppSkuStockClient;
import com.markerhub.satoken.utils.StpUserUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class AppCartItemServiceImpl extends ServiceImpl<AppCartItemMapper, AppCartItem>
		implements AppCartItemService {

	@Resource
	AppCartItemMapper appCartItemMapper;

	@Resource
	AppSkuStockClient appSkuStockClient;

	@Resource
	AppProductClient appProductClient;

	@Override
	public List<AppCartItem> getCurrent() {

		long userId = StpUserUtil.getLoginIdAsLong();

		return appCartItemMapper.getCartItemsWithProductInfo(new QueryWrapper<AppCartItem>()
				.eq("user_id", userId)
				.orderByDesc("created")
		);
	}

	@Override
	public long getCount() {
		long userId = StpUserUtil.getLoginIdAsLong();
		return this.count(new QueryWrapper<AppCartItem>().eq("user_id", userId));
	}

	@Override
	public Object getTotal(ArrayList<Long> ids) {

		long userId = StpUserUtil.getLoginIdAsLong();

		// 获取选中的购物车
		List<AppCartItem> cartItems = new ArrayList<>();
		if (!ids.isEmpty()) {
			cartItems = appCartItemMapper.getCartItemsWithProductInfo(new QueryWrapper<AppCartItem>()
					.eq("user_id", userId)
					.in("c.id", ids)
					.orderByDesc("created")
			);
		}

		// 计算总金额，价格、数量
		BigDecimal total = cartItems.stream()
				.map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// 运费、优惠、满减

		return MapUtil.of("total", total);
	}

	@Override
	public void updateQuantity(Long cartItemId, Integer quantity) {
		long userId = StpUserUtil.getLoginIdAsLong();
		AppCartItem cartItem = this.getById(cartItemId);

		// 判断库存
		AppSkuStock skuStock = appSkuStockClient.getSku(cartItem.getSkuId(), cartItem.getProductId()).getData();
		Assert.isTrue(skuStock.getStock() >= quantity, "库存不足");

		// 更新购买数量
		this.update(new UpdateWrapper<AppCartItem>()
				.eq("id", cartItemId)
				.eq("user_id", userId)
				.set("quantity", quantity)
				.set("updated", LocalDateTime.now())
		);

	}

	@Override
	public void add(AppCartItem appCartItem) {

		long userId = StpUserUtil.getLoginIdAsLong();

		// 判断商品是否为空、是否上架
		AppProduct product = appProductClient.getById(appCartItem.getProductId()).getData();
		Assert.isTrue(product != null && product.getIsOnSale(), "该商品已下架");

		// 判断库存是否足够
		AppSkuStock skuStock = appSkuStockClient.getSku(appCartItem.getSkuId(), appCartItem.getProductId()).getData();
		Assert.isTrue(skuStock.getStock() >= appCartItem.getQuantity(), "库存不足");


		// 获取已存在的购物车商品
		AppCartItem existCartItem = this.getOne(new QueryWrapper<AppCartItem>()
				.eq("user_id", userId)
				.eq("product_id", appCartItem.getProductId())
				.eq("sku_id", appCartItem.getSkuId())
		);

		// 购物车已存在该商品
		if (existCartItem != null) {

			// 原来数量加上新购买数量
			existCartItem.setQuantity(existCartItem.getQuantity() + appCartItem.getQuantity());

			existCartItem.setUpdated(LocalDateTime.now());
			this.updateById(existCartItem);
		} else {
			// 购物车未存在该商品

			appCartItem.setCreated(LocalDateTime.now());
			appCartItem.setUserId(userId);

			this.save(appCartItem);
		}
	}

	@Override
	public AppCartItem getCombinedCartItem(long userId, Long productId, Long skuId, Integer quantity) {
		AppProduct product = appProductClient.getById(productId).getData();
		Assert.isTrue(product != null && product.getIsOnSale(), "商品已下架");

		AppCartItem item = new AppCartItem();

		item.setProductId(productId);
		item.setSkuId(skuId);
		item.setQuantity(quantity);

		item.setProductName(product.getName());
		item.setProductImage(product.getImage());
		item.setProductSn(product.getSn());
		item.setCategoryId(product.getCategoryId());

		item.setUserId(userId);

		if (skuId == -1) {
			item.setPrice(product.getPrice());
			item.setSku("默认规格");
		} else {
			AppSkuStock skuStock = appSkuStockClient.getSku(skuId, -1L).getData();
			item.setSku(skuStock.getSku());
			item.setPrice(skuStock.getPrice());
		}
		return item;
	}

}




