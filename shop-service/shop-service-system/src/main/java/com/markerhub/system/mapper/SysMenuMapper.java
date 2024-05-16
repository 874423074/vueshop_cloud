package com.markerhub.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markerhub.system.entity.SysMenu;

import java.util.List;

/**
 * @Entity com.markerhub.entity.SysMenu
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

	List<Long> getMenuIdsByUserId(Long userId);
}




