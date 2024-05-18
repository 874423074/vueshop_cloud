package com.markerhub.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markerhub.product.entity.AppSkuStock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @Entity com.markerhub.entity.AppSkuStock
 */
public interface AppSkuStockMapper extends BaseMapper<AppSkuStock> {

	@Update("update app_sku_stock set stock = stock - #{quantity} where id = #{skuId} and stock >= #{quantity}")
	int reduceStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

	@Update("update app_sku_stock set stock = stock + #{quantity} where id = #{skuId} and stock >= 0")
	int releaseStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);
}




