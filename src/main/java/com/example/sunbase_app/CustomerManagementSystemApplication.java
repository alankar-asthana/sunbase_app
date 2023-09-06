package com.example.sunbase_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.example.sunbase_app.controller")
@ComponentScan(basePackages = "com.example.sunbase_app")
public class CustomerManagementSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(CustomerManagementSystemApplication.class, args);
	}

}
