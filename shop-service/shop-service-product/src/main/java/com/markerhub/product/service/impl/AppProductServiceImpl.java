package com.markerhub.product.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.product.dto.ProductQuantityDto;
import com.markerhub.product.entity.AppCategory;
import com.markerhub.product.entity.AppProduct;
import com.markerhub.product.entity.AppSkuStock;
import com.markerhub.product.entity.AppSpecificationValue;
import com.markerhub.product.mapper.AppProductMapper;
import com.markerhub.product.service.AppCategoryService;
import com.markerhub.product.service.AppProductService;
import com.markerhub.product.service.AppSkuStockService;
import com.markerhub.product.service.AppSpecificationValueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Service
public class AppProductServiceImpl extends ServiceImpl<AppProductMapper, AppProduct>
		implements AppProductService {

	@Resource
	AppCategoryService appCategoryService;

	@Resource
	AppSpecificationValueService appSpecificationValueService;

	@Resource
	AppSkuStockService appSkuStockService;

	@Resource
	AppProductMapper appProductMapper;

	@Override
	public Object pageByCategoryId(Page page, Long categoryId, Integer sort) {

		AppCategory category = appCategoryService.getById(categoryId);
		Assert.notNull(category, "商品分类不存在");

		Page pageData = this.page(page, new QueryWrapper<AppProduct>()
				.eq("is_on_sale", true)
				.eq("category_id", categoryId)

				.orderByDesc(sort == 0, "is_top", "sort_order")
				.orderByDesc(sort == 1, "created")
				.orderByDesc(sort == 2, "sale")
		);

		return MapUtil.builder()
				.put("category", category)
				.put("pageData", pageData)
				.build();
	}

	@Override
	public AppProduct getWithAttrsById(Long productId) {

		// 商品详情
		AppProduct product = this.getById(productId);
		Assert.notNull(product, "商品不存在");

		// 商品属性
		List<AppSpecificationValue> specificationValues = appSpecificationValueService.listByProductId(productId);
		product.setSpecifications(specificationValues);

		// 商品可选组合（sku stock 、price ） sku: 商品库存最小的存货单位。
		List<AppSkuStock> skuStocks = appSkuStockService.listByProductId(productId);
		product.setSkuStocks(skuStocks);

		return product;
	}

	@Override
	public Page<AppProduct> pageWithCategory(Page page, String name, Long categoryId) {
		return appProductMapper.pageWithCategory(page, name, categoryId);
	}

	@Override
	public void create(AppProduct appProduct) {
		if (appProduct.getId() == null) {
			appProduct.setCreated(LocalDateTime.now());
			appProduct.setSn(generateProductSn());
		} else {
			appProduct.setUpdated(LocalDateTime.now());
		}

		this.saveOrUpdate(appProduct);
	}

	private String generateProductSn() {
		String dateStr = DateUtil.format(new Date(), "yyMMddHHmmss");
		return "PD" + dateStr + RandomUtil.randomNumbers(4);
	}

	/**
	 * 更新商品销量
	 */
	public void updateProductSale(List<ProductQuantityDto> dtos) {

		List<AppProduct> products = new ArrayList<>();
		dtos.forEach(item -> {

			AppProduct appProduct = this.getById(item.getProductId());
			appProduct.setSale(appProduct.getSale() + item.getQuantity());
			products.add(appProduct);
		});

		this.updateBatchById(products);
	}
}




