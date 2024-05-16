package com.markerhub.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.system.entity.SysUser;
import com.markerhub.system.entity.SysUser;

/**
 *
 */
public interface SysUserService extends IService<SysUser> {

	Page<SysUser> getPage(Page page, SysUser sysUser);

	void deleteBath(Long[] ids);

	void role(Long userId, Long... roleIds);
}
