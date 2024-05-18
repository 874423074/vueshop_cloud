package com.markerhub.order;

import com.markerhub.feign.annotation.EnableShopFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableShopFeignClients
@SpringBootApplication
public class ShopOrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShopOrderApplication.class, args);
	}
}