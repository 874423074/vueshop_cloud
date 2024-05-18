package com.markerhub.product.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.product.dto.AppSkuDto;
import com.markerhub.core.exception.HubException;
import com.markerhub.product.dto.ProductQuantityDto;
import com.markerhub.product.entity.AppProduct;
import com.markerhub.product.entity.AppSkuStock;
import com.markerhub.product.entity.AppSpecification;
import com.markerhub.product.entity.AppSpecificationValue;
import com.markerhub.product.mapper.AppProductMapper;
import com.markerhub.product.mapper.AppSkuStockMapper;
import com.markerhub.product.service.AppSkuStockService;
import com.markerhub.product.service.AppSpecificationService;
import com.markerhub.product.service.AppSpecificationValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class AppSkuStockServiceImpl extends ServiceImpl<AppSkuStockMapper, AppSkuStock>
		implements AppSkuStockService {

	@Resource
	AppProductMapper appProductMapper;

	@Resource
	AppSkuStockMapper appSkuStockMapper;

	@Resource
	AppSpecificationService appSpecificationService;

	@Resource
	AppSpecificationValueService appSpecificationValueService;

	@Override
	public List<AppSkuStock> listByProductId(Long productId) {

		// 多规格产品
		List<AppSkuStock> skuStocks = this.list(new QueryWrapper<AppSkuStock>().eq("product_id", productId));

		// 单规格产品
		if (skuStocks == null || skuStocks.size() == 0) {
			skuStocks = ListUtil.toList(getSingleSkuStock(productId));
		}

		return skuStocks;
	}

	@Override
	public AppSkuStock getSku(Long skuId, Long productId) {

		if (skuId != -1) {
			// 多规格库存
			return this.getById(skuId);
		}
		// 单规格库存
		return getSingleSkuStock(productId);
	}

	@Override
	public void reduceStock(List<ProductQuantityDto> dtos) {

		dtos.forEach(item -> {
			int count;
			if (item.getSkuId() == -1) {
				// 默认规格
				count = appProductMapper.reduceStock(item.getProductId(), item.getQuantity());
			} else {
				// 多规格
				count = appSkuStockMapper.reduceStock(item.getSkuId(), item.getQuantity());
			}
			if (count == 0) {
				throw new HubException("库存不足");
			}
		});
	}

	@Override
	public void releaseStock(List<ProductQuantityDto> dtos) {

		dtos.forEach(item -> {
			int count;
			if (item.getSkuId() == -1) {
				// 默认规格
				count = appProductMapper.releaseStock(item.getProductId(), item.getQuantity());
			} else {
				// 多规格
				count = appSkuStockMapper.releaseStock(item.getSkuId(), item.getQuantity());
			}
			if (count == 0) {
				throw new HubException("释放库存失败");
			}
		});
	}

	@Override
	public Object getSkuByProductId(Long productId) {

		// 商品信息
		AppProduct product = appProductMapper.selectById(productId);
		Assert.notNull(product, "商品不存在");

		// 获取所有内置规格
		List<AppSpecification> specs = appSpecificationService.list();

		// 获取该商品对应的规格值
		List<AppSpecificationValue> specValues = appSpecificationValueService.list(new QueryWrapper<AppSpecificationValue>()
				.eq("product_id", productId));


		// 获取规格值的名称组合
		List<String> selectSpecValues = specValues.stream().map(AppSpecificationValue::getSpec).collect(Collectors.toList());

		// 获取商品sku的信息
		List<AppSkuStock> skuStocks = this.listByProductId(productId);

		return MapUtil.builder()
				.put("product", product)
				.put("specs", specs)
				.put("specValues", specValues)
				.put("selectSpecValues", selectSpecValues)
				.put("skuStocks", skuStocks)
				.build();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveSku(AppSkuDto skuDto) {

		// 保存商品规格值
		List<AppSpecificationValue> specValues = skuDto.getSpecValues();

		// 先删除原来的值，再保存新的
		appSpecificationValueService.remove(new QueryWrapper<AppSpecificationValue>()
				.eq("product_id", skuDto.getProductId()));
		if (specValues == null || specValues.size() == 0) {
			System.out.println("默认spec，不需要保存");
		} else {
			specValues.forEach(e -> e.setCreated(LocalDateTime.now()));
			appSpecificationValueService.saveBatch(specValues);
		}

		// 保存skuStock
		// 先更新保存原来的，再删除多余的
		List<AppSkuStock> skuStocks = skuDto.getSkuStocks();
		if (skuStocks != null && skuStocks.size() > 0) {
			skuStocks.forEach(appSkuStock -> {
				if (appSkuStock.getId() == null) {
					appSkuStock.setCreated(LocalDateTime.now());
				} else {
					appSkuStock.setUpdated(LocalDateTime.now());
				}
			});
			this.saveOrUpdateBatch(skuStocks);
		}
		// 还存在的sku的ids
		Set<Long> skuIds = skuStocks.stream().map(AppSkuStock::getId).collect(Collectors.toSet());
		this.remove(new QueryWrapper<AppSkuStock>()
				.eq("product_id", skuDto.getProductId())
				.notIn(!skuIds.isEmpty(), "id", skuIds)
		);
	}

	// 商品单规格
	private AppSkuStock getSingleSkuStock(long productId) {
		AppProduct product = appProductMapper.selectById(productId);

		AppSkuStock skuStock = new AppSkuStock();
		skuStock.setPrice(product.getPrice());
		skuStock.setStock(product.getStock());

		skuStock.setSku("默认规格");
		skuStock.setId(-1L);

		return skuStock;
	}
}




