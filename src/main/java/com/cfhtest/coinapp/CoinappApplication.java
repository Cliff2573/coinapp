package com.cfhtest.coinapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CoinappApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinappApplication.class, args);
	}

}
