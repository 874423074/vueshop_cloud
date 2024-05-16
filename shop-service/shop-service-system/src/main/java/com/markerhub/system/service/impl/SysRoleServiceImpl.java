package com.markerhub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.system.entity.SysRole;
import com.markerhub.system.entity.SysRoleMenu;
import com.markerhub.system.entity.SysUserRole;
import com.markerhub.system.mapper.SysRoleMapper;
import com.markerhub.system.service.SysRoleMenuService;
import com.markerhub.system.service.SysRoleService;
import com.markerhub.system.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService{

	@Resource
	SysUserRoleService sysUserRoleService;

	@Resource
	SysRoleMenuService sysRoleMenuService;

	@Override
	public List<SysRole> listRolesByUserId(Long userId) {
		return this.list(new QueryWrapper<SysRole>()
				.inSql("id", "select role_id from sys_user_role where user_id = " + userId)
		);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBath(Long... ids) {
		this.removeBatchByIds(Arrays.asList(ids));

		// 同步删除中间表
		sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id", Arrays.asList(ids)));
		sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id", Arrays.asList(ids)));
	}

	/**
	 * 分配权限
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void perm(Long roleId, Long... menuIds) {
		List<SysRoleMenu> sysRoleMenus = new ArrayList<>();

		for (Long menuId : menuIds) {
			SysRoleMenu roleMenu = new SysRoleMenu();
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(menuId);

			sysRoleMenus.add(roleMenu);
		}

		// 先删除原来的关系
		sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id",roleId));

		sysRoleMenuService.saveBatch(sysRoleMenus);
	}
}




