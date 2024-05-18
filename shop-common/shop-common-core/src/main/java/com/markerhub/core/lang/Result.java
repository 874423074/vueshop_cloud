package com.markerhub.core.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

	//	结果状态（如200表示成功，400表示异常）
	private int code;
	//	结果消息
	private String msg;
	//	结果数据，为了让服务之间的调用知道data的类型，所以需要把object改成泛型
	private T data;

	public static <T> Result<T> success() {
		return success(null);
	}

	public static <T> Result<T> success(T data) {
		Result<T> result = new Result<>();
		result.setCode(200);
		result.setMsg("操作成功");
		result.setData(data);
		return result;
	}

	public static <T> Result<T> fail(String msg) {
		return fail(msg, null);
	}

	public static <T> Result<T> fail(String msg, T data) {
		Result<T> result = new Result<>();
		result.setCode(400);
		result.setMsg(msg);
		result.setData(data);
		return result;
	}

}
