package com.markerhub.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.system.entity.SysUser;
import com.markerhub.system.entity.SysUserRole;
import com.markerhub.system.mapper.SysUserMapper;
import com.markerhub.system.service.SysRoleService;
import com.markerhub.system.service.SysUserRoleService;
import com.markerhub.system.service.SysUserService;
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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

	@Resource
	SysRoleService sysRoleService;

	@Resource
	SysUserRoleService sysUserRoleService;

	@Override
	public Page<SysUser> getPage(Page page, SysUser sysUser) {
		Page<SysUser> pageData = this.page(page, new QueryWrapper<SysUser>()
				.like(StrUtil.isNotBlank(sysUser.getUsername()), "name", sysUser.getUsername())
				.orderByDesc("created"));

		pageData.getRecords().forEach(u -> {
			u.setRoles(sysRoleService.listRolesByUserId(u.getId()));
		});

		return pageData;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBath(Long[] ids) {
		this.removeBatchByIds(Arrays.asList(ids));

		// 同步删除中间表
		sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", Arrays.asList(ids)));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void role(Long userId, Long... roleIds) {
		List<SysUserRole> sysUserRoles = new ArrayList<>();

		for (Long roleId : roleIds) {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(userId);
			userRole.setRoleId(roleId);

			sysUserRoles.add(userRole);
		}

		// 先删除原来的关系
		sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id",userId));

		sysUserRoleService.saveBatch(sysUserRoles);
	}
}




