package com.markerhub.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.product.entity.AppProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @Entity com.markerhub.entity.AppProduct
 */
public interface AppProductMapper extends BaseMapper<AppProduct> {

	@Update("update app_product set stock = stock - #{quantity} where id = #{productId} and stock >= #{quantity}")
	int reduceStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

	@Update("update app_product set stock = stock + #{quantity} where id = #{productId} and stock >= 0")
	int releaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

	Page<AppProduct> pageWithCategory(Page page, String name, Long categoryId);
}




