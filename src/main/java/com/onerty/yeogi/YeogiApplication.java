package com.onerty.yeogi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YeogiApplication {

	public static void main(String[] args) {
		SpringApplication.run(YeogiApplication.class, args);
	}

}
