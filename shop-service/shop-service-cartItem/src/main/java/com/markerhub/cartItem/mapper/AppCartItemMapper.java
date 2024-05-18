package com.markerhub.cartItem.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.markerhub.cartItem.entity.AppCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.markerhub.entity.AppCartItem
 */
public interface AppCartItemMapper extends BaseMapper<AppCartItem> {

	List<AppCartItem> getCartItemsWithProductInfo(@Param(Constants.WRAPPER) QueryWrapper<AppCartItem> wrapper);
}




