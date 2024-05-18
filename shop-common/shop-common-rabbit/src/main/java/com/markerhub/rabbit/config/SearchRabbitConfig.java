package com.markerhub.rabbit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchRabbitConfig {

	public final static String SEARCH_EXCHANGE = "product-search-exchange";
	public final static String PRODUCT_SEARCH_CREATE_QUEUE = "product-search-create-queue";
	public final static String PRODUCT_SEARCH_DELETE_QUEUE = "product-search-delete-queue";
	public final static String PRODUCT_SEARCH_CREATE_KEY = "product.search.create";
	public final static String PRODUCT_SEARCH_DELETE_KEY = "product.search.delete";

	@Bean
	public TopicExchange searchExchange() {
		return new TopicExchange(SEARCH_EXCHANGE, true, false);
	}

	@Bean
	public Queue searchCreateQueue() {
		return new Queue(PRODUCT_SEARCH_CREATE_QUEUE, true);
	}

	@Bean
	public Queue searchDeleteQueue() {
		return new Queue(PRODUCT_SEARCH_DELETE_QUEUE, true);
	}

	@Bean
	public Binding searchCreateQueueBinding() {
		return BindingBuilder.bind(searchCreateQueue()).to(searchExchange()).with(PRODUCT_SEARCH_CREATE_KEY);
	}

	@Bean
	public Binding searchDeleteQueueBinding() {
		return BindingBuilder.bind(searchDeleteQueue()).to(searchExchange()).with(PRODUCT_SEARCH_DELETE_KEY);
	}

}
