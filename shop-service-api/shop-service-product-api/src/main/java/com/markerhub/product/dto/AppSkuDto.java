package com.markerhub.product.dto;


import com.markerhub.product.entity.AppSkuStock;
import com.markerhub.product.entity.AppSpecificationValue;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class AppSkuDto implements Serializable {

	@NotNull(message = "商品ID不能为空")
	private Long productId;

	private List<AppSpecificationValue> specValues;

	private List<AppSkuStock> skuStocks;

}
