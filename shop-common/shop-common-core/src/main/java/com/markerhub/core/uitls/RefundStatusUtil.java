package com.markerhub.core.uitls;

import cn.hutool.core.map.MapUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RefundStatusUtil {

	private final static Map<Integer, String> statusMap = new HashMap<>();

	static {
		statusMap.put(0, "待处理");
		statusMap.put(1, "退货中");
		statusMap.put(2, "已完成");
		statusMap.put(3, "已拒绝");
	}

	public static String invert(int status) {
		return statusMap.get(status);
	}

	public static List<Map> getStatusList() {
		return statusMap.entrySet().stream()
				.map(e ->
						MapUtil.builder().put("key", e.getKey()).put("value", e.getValue()).build()
				).collect(Collectors.toList());
	}

}
