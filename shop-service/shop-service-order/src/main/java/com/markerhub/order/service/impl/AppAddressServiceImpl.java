package com.markerhub.order.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.order.entity.AppAddress;
import com.markerhub.order.mapper.AppAddressMapper;
import com.markerhub.order.service.AppAddressService;
import com.markerhub.satoken.utils.StpUserUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 *
 */
@Service
public class AppAddressServiceImpl extends ServiceImpl<AppAddressMapper, AppAddress>
		implements AppAddressService {

	@Override
	public AppAddress getDefault(long userId) {
		return this.getOne(new QueryWrapper<AppAddress>()
				.eq("is_default", true)
				.eq("user_id", userId));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveAndDefault(AppAddress appAddress) {
		long userId = StpUserUtil.getLoginIdAsLong();

		if (appAddress.getId() == null) {
			appAddress.setUserId(userId);
			appAddress.setCreated(LocalDateTime.now());
		} else {

			long count = this.count(new QueryWrapper<AppAddress>()
					.eq("user_id", userId)
					.eq("id", appAddress.getId()));

			Assert.isTrue(count > 0, "收货地址不存在");

			appAddress.setUpdated(LocalDateTime.now());
		}

		// 如果是默认地址，则把以前的默认地址改为非默认地址
		if (appAddress.getIsDefault()) {
			this.update(new UpdateWrapper<AppAddress>()
					.set("is_default", false)
					.eq("user_id", userId)
					.eq("is_default", true)
			);
		}

		this.saveOrUpdate(appAddress);
	}
}




