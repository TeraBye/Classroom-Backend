package com.example.import_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
public class ImportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImportServiceApplication.class, args);
	}

}
