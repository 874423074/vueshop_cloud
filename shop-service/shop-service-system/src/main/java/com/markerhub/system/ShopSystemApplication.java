package com.markerhub.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.markerhub.system.feign")
@SpringBootApplication
public class ShopSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShopSystemApplication.class, args);
	}
}