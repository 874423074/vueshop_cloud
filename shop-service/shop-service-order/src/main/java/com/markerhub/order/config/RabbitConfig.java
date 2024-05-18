package com.markerhub.order.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	public final static String ORDER_CANCEL_EXCHANGE = "order-cancel-exchange";
	public final static String ORDER_CANCEL_ROUTE_KEY = "order-cancel-route-key";
	public final static String ORDER_CANCEL_QUEUE = "order-cancel-queue";

	@Bean
	public DirectExchange orderCancelExchange() {
		return new DirectExchange(ORDER_CANCEL_EXCHANGE, true, false);
	}

	@Bean
	public Queue orderCancelQueue() {
		return QueueBuilder.durable(ORDER_CANCEL_QUEUE)
				// 绑定死信队列
				.deadLetterExchange(TTL_ORDER_CANCEL_EXCHANGE)
				.deadLetterRoutingKey(TTL_ORDER_CANCEL_ROUTE_KEY)
				.build();
	}

	@Bean
	Binding orderCancelBinding() {
		return BindingBuilder.bind(orderCancelQueue())
				.to(orderCancelExchange())
				.with(ORDER_CANCEL_ROUTE_KEY);
	}


	// 死信队列
	public final static String TTL_ORDER_CANCEL_EXCHANGE = "ttl-order-cancel-exchange";
	public final static String TTL_ORDER_CANCEL_ROUTE_KEY = "ttl-order-cancel-route-key";
	public final static String TTL_ORDER_CANCEL_QUEUE = "ttl-order-cancel-queue";

	@Bean
	public DirectExchange ttlOrderCancelExchange() {
		return new DirectExchange(TTL_ORDER_CANCEL_EXCHANGE, true, false);
	}

	@Bean
	public Queue ttlOrderCancelQueue() {
		return QueueBuilder.durable(TTL_ORDER_CANCEL_QUEUE).build();
	}

	@Bean
	Binding ttlOrderCancelBinding() {
		return BindingBuilder.bind(ttlOrderCancelQueue())
				.to(ttlOrderCancelExchange())
				.with(TTL_ORDER_CANCEL_ROUTE_KEY);
	}


}
