package com.markerhub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.system.entity.SysRole;

import java.util.List;

/**
 *
 */
public interface SysRoleService extends IService<SysRole> {

	List<SysRole> listRolesByUserId(Long userId);

	void deleteBath(Long... ids);

	void perm(Long roleId, Long... menuIds);
}
