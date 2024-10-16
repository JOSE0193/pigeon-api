package com.syonet.pigeon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PigeonApplication {

	public static void main(String[] args) {

		SpringApplication.run(PigeonApplication.class, args);
	}

}
