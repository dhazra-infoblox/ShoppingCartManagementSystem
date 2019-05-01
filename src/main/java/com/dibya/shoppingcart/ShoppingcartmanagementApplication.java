package com.dibya.shoppingcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.dibya.shoppingcart" })
public class ShoppingcartmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingcartmanagementApplication.class, args);
	}

}
