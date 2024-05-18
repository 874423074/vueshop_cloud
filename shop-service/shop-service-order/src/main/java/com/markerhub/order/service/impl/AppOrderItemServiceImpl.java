package com.markerhub.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.order.entity.AppOrderItem;
import com.markerhub.order.mapper.AppOrderItemMapper;
import com.markerhub.order.service.AppOrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class AppOrderItemServiceImpl extends ServiceImpl<AppOrderItemMapper, AppOrderItem>
    implements AppOrderItemService{

	@Override
	public List<AppOrderItem> listByOrderId(Long orderId) {
		return this.list(new QueryWrapper<AppOrderItem>().eq("order_id", orderId));
	}

}




