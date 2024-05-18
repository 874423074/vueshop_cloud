package com.markerhub.rabbit.lang;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchRabbitConst {

	public final static String SEARCH_EXCHANGE = "product-search-exchange";
	public final static String PRODUCT_SEARCH_CREATE_QUEUE = "product-search-create-queue";
	public final static String PRODUCT_SEARCH_DELETE_QUEUE = "product-search-delete-queue";
	public final static String PRODUCT_SEARCH_CREATE_KEY = "product.search.create";
	public final static String PRODUCT_SEARCH_DELETE_KEY = "product.search.delete";
}
