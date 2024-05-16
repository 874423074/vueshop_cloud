package com.markerhub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.system.dto.SysMenuDto;
import com.markerhub.system.entity.SysMenu;

import java.util.List;

/**
 *
 */
public interface SysMenuService extends IService<SysMenu> {

	List<SysMenu> listMenusByUserId(Long userId);

	List<SysMenuDto> getCurrentUserNav();

	String getUserPerms(Long userId);

	List<SysMenu> tree();

	void delete(Long id);
}
