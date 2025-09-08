package com.joaovidal.receitron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ReceitronApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReceitronApplication.class, args);
	}

}
