package com.markerhub.cartItem;

import com.markerhub.feign.annotation.EnableShopFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableShopFeignClients
@SpringBootApplication
public class ShopCartItemApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShopCartItemApplication.class, args);
	}
}