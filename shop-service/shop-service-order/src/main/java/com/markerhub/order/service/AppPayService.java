package com.markerhub.order.service;

public interface AppPayService {
	Object pay(String orderSn);

	Object payCheck(String orderSn);

	void refund(Long refundId);
}
