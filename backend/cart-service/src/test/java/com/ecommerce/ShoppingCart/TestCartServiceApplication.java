package com.ecommerce.ShoppingCart;

import org.springframework.boot.SpringApplication;

public class TestCartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ShoppingCartApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
