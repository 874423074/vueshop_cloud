package com.markerhub.search;

import com.markerhub.feign.annotation.EnableShopFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableShopFeignClients
@SpringBootApplication
public class ShopSearchApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShopSearchApplication.class, args);
	}
}