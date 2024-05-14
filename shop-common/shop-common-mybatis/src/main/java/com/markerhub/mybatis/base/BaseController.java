package com.markerhub.mybatis.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.ServletRequestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class BaseController {

	@Resource
	HttpServletRequest req;

	public Page getPage() {
		int current = ServletRequestUtils.getIntParameter(req, "current", 1);
		int size = ServletRequestUtils.getIntParameter(req, "size", 10);

		return new Page(current, size);
	}

}
