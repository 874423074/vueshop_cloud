package com.markerhub.order.mq;

import com.markerhub.order.config.RabbitConfig;
import com.markerhub.order.service.AppOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class MqReceiver {

	@Resource
	AppOrderService appOrderService;

	@RabbitListener(queues = RabbitConfig.TTL_ORDER_CANCEL_QUEUE)
	public void cancelOrderHandle(long orderId) {

		// 取消订单
		appOrderService.closeAdmin(orderId, "支付超时自动取消", 2L);

		log.info("正在取消订单 - {}", orderId);
	}

}
