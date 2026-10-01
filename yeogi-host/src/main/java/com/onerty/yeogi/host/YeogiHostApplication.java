package com.onerty.yeogi.host;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.onerty.yeogi"})
public class YeogiHostApplication {

    public static void main(String[] args) {
        SpringApplication.run(YeogiHostApplication.class, args);
    }

}
