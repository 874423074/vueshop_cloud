package com.markerhub.product.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductQuantityDto implements Serializable {

	private long skuId;
	private long productId;
	private int quantity;

}
