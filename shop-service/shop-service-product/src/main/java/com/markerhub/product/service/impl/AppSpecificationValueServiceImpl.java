package com.markerhub.product.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.product.entity.AppSpecificationValue;
import com.markerhub.product.mapper.AppSpecificationValueMapper;
import com.markerhub.product.service.AppSpecificationValueService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class AppSpecificationValueServiceImpl extends ServiceImpl<AppSpecificationValueMapper, AppSpecificationValue>
    implements AppSpecificationValueService{

	@Override
	public List<AppSpecificationValue> listByProductId(Long productId) {

		// 多种规格
		List<AppSpecificationValue> specificationValues = this.list(new QueryWrapper<AppSpecificationValue>().eq("product_id", productId));
		if (specificationValues == null || specificationValues.size() == 0) {

			// 单规格商品
			specificationValues = getSingleSpec();
		}

		return specificationValues;
	}

	private List<AppSpecificationValue> getSingleSpec() {

		AppSpecificationValue value = new AppSpecificationValue();
		value.setSpec("规格");
		value.setValue("默认规格");
		return ListUtil.toList(value);
	}
}




