package com.markerhub.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.order.dto.OrderDto;
import com.markerhub.order.entity.AppOrder;

/**
 *
 */
public interface AppOrderService extends IService<AppOrder> {

	Object preview(OrderDto orderDto);

	Object create(OrderDto orderDto);

	AppOrder getBySn(String sn);

	AppOrder getOwnBySn(String sn);
	AppOrder getOwnById(Long id);

	Object getOrderCount();

	Object getPage(Page page, long userId, Integer status);

	Object detail(Long id);

	void cancel(Long id);

	void delete(Long id);

	Object getDeliveryInfo(AppOrder order) throws Exception;

	void confirm(Long id);

	Object pageAdmin(Page page, AppOrder appOrder);

	Object getOrderInfo(Long id);

	void shipAdmin(AppOrder appOrder);

	void closeAdmin(Long id, String adminNote, Long adminId);
}
