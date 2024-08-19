package com.fujitsu.delivery_fee_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = "com.fujitsu.delivery_fee_api")
@EnableScheduling
public class DeliveryFeeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryFeeApiApplication.class, args);
	}

}
