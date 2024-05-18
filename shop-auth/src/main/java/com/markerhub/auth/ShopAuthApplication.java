package com.markerhub.auth;

import com.markerhub.feign.annotation.EnableShopFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableShopFeignClients
@SpringBootApplication
public class ShopAuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShopAuthApplication.class, args);
	}
}