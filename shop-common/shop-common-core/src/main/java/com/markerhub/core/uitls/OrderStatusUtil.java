package com.markerhub.core.uitls;

import cn.hutool.core.map.MapUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderStatusUtil {

	private final static Map<Integer, String> statusMap = new HashMap<>();

	static {
		statusMap.put(0, "待付款");
		statusMap.put(1, "待发货");
		statusMap.put(2, "待收货");
		statusMap.put(3, "已完成");
		statusMap.put(4, "已取消");
		statusMap.put(5, "退款中");
		statusMap.put(6, "已退款");
		statusMap.put(7, "退款失败");
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
