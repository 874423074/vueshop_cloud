package com.markerhub.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.system.dto.SysMenuDto;
import com.markerhub.system.entity.SysMenu;
import com.markerhub.system.entity.SysRoleMenu;
import com.markerhub.system.mapper.SysMenuMapper;
import com.markerhub.system.service.SysMenuService;
import com.markerhub.system.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService{

	@Resource
	SysMenuMapper sysMenuMapper;

	@Resource
	SysRoleMenuService sysRoleMenuService;

	@Override
	public List<SysMenu> listMenusByUserId(Long userId) {

		// 用户id为1的超级管理员

		// 管理员 角色 菜单
		List<Long> menuIds = sysMenuMapper.getMenuIdsByUserId(userId);
		if (userId != 1 && menuIds.size() == 0) {
			return new ArrayList<>();
		}

		return this.list(new QueryWrapper<SysMenu>()
				.in(userId != 1, "id", menuIds)
				.eq("status", 0)
				.orderByDesc("sort_order")
		);
	}

	@Override
	public List<SysMenuDto> getCurrentUserNav() {
		List<SysMenu> menus = this.listMenusByUserId(StpUtil.getLoginIdAsLong());
		List<SysMenu> sysMenus = buildTreeMenu(menus);
		return BeanUtil.copyToList(sysMenus, SysMenuDto.class);
	}

	@Override
	public String getUserPerms(Long userId) {

		List<SysMenu> menus = this.listMenusByUserId(userId);
		return menus.stream().map(SysMenu::getPerms).collect(Collectors.joining(","));
	}

	@Override
	public List<SysMenu> tree() {
		List<SysMenu> menus = this.list(new QueryWrapper<SysMenu>()
				.orderByDesc("sort_order"));

		return buildTreeMenu(menus);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {

		// 判断是否有子级
		long count = this.count(new QueryWrapper<SysMenu>().eq("parent_id", id));
		Assert.isTrue(count == 0, "请先删除子菜单");

		removeById(id);

		// 同步删除roleMenu中间表的记录
		sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id", id));
	}

	/**
	 * 构建菜单的树状结构
	 */
	private List<SysMenu> buildTreeMenu(List<SysMenu> menus) {

		List<SysMenu> finalMenus = new ArrayList<>();

		for (SysMenu menu : menus) {

			// 找出当前menu的子级
			for (SysMenu e : menus) {
				if (menu.getId().longValue() == e.getParentId()) {
					menu.getChildren().add(e);
				}
			}

			// 提取顶级节点
			if (menu.getParentId() == 0L) {
				finalMenus.add(menu);
			}
		}
		return finalMenus;
	}
}




