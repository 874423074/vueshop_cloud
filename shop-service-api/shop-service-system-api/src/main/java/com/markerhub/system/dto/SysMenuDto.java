package com.markerhub.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SysMenuDto implements Serializable {

	private Long id;

	private String title;
	private String icon;
	private String path;
	private String perms;
	private String component;
	private String name;

	List<SysMenuDto> children = new ArrayList<>();

	public String getName() {
		return perms;
	}

}
