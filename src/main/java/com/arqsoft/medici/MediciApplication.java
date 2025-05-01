package com.arqsoft.medici;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MediciApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediciApplication.class, args);
	}

}
