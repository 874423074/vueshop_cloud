package com.markerhub.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.order.entity.AppAddress;

/**
 *
 */
public interface AppAddressService extends IService<AppAddress> {

	AppAddress getDefault(long userId);

	void saveAndDefault(AppAddress appAddress);
}
