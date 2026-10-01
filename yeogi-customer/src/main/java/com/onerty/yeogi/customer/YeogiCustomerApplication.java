package com.onerty.yeogi.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.onerty.yeogi"})
public class YeogiCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(YeogiCustomerApplication.class, args);
    }

}
