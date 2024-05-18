package com.markerhub.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.order.entity.AppOrder;

/**
 * @Entity com.markerhub.entity.AppOrder
 */
public interface AppOrderMapper extends BaseMapper<AppOrder> {

	Page<AppOrder> pageWithUsername(Page page, AppOrder appOrder);
}




