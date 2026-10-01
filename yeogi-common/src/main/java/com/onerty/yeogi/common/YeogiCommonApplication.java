package com.onerty.yeogi.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YeogiCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(YeogiCommonApplication.class, args);
    }

}
