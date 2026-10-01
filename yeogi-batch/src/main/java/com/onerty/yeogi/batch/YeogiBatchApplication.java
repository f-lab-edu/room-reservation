package com.onerty.yeogi.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.onerty.yeogi"})
public class YeogiBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(YeogiBatchApplication.class, args);
    }

}
