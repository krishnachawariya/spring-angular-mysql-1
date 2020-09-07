package com.example.springangular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringAngularMysql1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringAngularMysql1Application.class, args);
	}

}
