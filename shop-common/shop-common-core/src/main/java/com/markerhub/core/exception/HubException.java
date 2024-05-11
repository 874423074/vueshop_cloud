package com.markerhub.core.exception;

public class HubException extends RuntimeException{

	private String msg;
	private int code = 500;

	public HubException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public HubException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
}
