package com.fujitsu.delivery_fee_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = "com.fujitsu.delivery_fee_api")
@ComponentScan(basePackages = "com.fujitsu.delivery_fee_api")
@EntityScan(basePackages = "com.fujitsu.delivery_fee_api.model")
public class DeliveryFeeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryFeeApiApplication.class, args);
	}

}
