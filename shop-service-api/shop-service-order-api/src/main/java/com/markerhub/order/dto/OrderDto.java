package com.markerhub.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderDto implements Serializable {

	private Long addressId;
	private List<Long> cartIds;
	private String note;

	private Long productId;
	private Long skuId;
	private Integer quantity;


}
