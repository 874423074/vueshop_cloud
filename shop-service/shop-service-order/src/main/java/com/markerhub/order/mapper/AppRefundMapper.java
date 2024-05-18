package com.markerhub.order.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.order.entity.AppRefund;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.markerhub.entity.AppRefund
 */
public interface AppRefundMapper extends BaseMapper<AppRefund> {

	Page<AppRefund> pageWithUsername(Page page, @Param(Constants.WRAPPER) QueryWrapper<AppRefund> wrapper);
}




