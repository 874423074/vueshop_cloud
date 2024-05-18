package com.markerhub.search.config;

import com.markerhub.rabbit.lang.SearchRabbitConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchRabbitConfig {

	@Bean
	public TopicExchange searchExchange() {
		return new TopicExchange(SearchRabbitConst.SEARCH_EXCHANGE, true, false);
	}

	@Bean
	public Queue searchCreateQueue() {
		return new Queue(SearchRabbitConst.PRODUCT_SEARCH_CREATE_QUEUE, true);
	}

	@Bean
	public Queue searchDeleteQueue() {
		return new Queue(SearchRabbitConst.PRODUCT_SEARCH_DELETE_QUEUE, true);
	}

	@Bean
	public Binding searchCreateQueueBinding() {
		return BindingBuilder.bind(searchCreateQueue()).to(searchExchange()).with(SearchRabbitConst.PRODUCT_SEARCH_CREATE_KEY);
	}

	@Bean
	public Binding searchDeleteQueueBinding() {
		return BindingBuilder.bind(searchDeleteQueue()).to(searchExchange()).with(SearchRabbitConst.PRODUCT_SEARCH_DELETE_KEY);
	}

}
