package com.markerhub.order.mq;


import com.markerhub.order.config.RabbitConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class MqSender {

	@Resource
	AmqpTemplate amqpTemplate;

	public void sendCancelOrderMessage(long orderId) {
		amqpTemplate.convertAndSend(RabbitConfig.ORDER_CANCEL_EXCHANGE, RabbitConfig.ORDER_CANCEL_ROUTE_KEY, orderId
				, message -> {
					// 30分钟后消息过期
					message.getMessageProperties().setExpiration(String.valueOf(30 * 60 * 1000));
					return message;
				}
		);
	}
}
