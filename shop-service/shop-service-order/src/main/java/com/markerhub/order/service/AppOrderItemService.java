package com.markerhub.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.order.entity.AppOrderItem;

import java.util.List;

/**
 *
 */
public interface AppOrderItemService extends IService<AppOrderItem> {

	List<AppOrderItem> listByOrderId(Long orderId);
}
