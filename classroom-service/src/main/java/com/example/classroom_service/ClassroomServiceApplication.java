package com.example.classroom_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ClassroomServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassroomServiceApplication.class, args);
	}

}
