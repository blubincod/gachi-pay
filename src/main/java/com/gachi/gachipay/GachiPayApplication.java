package com.gachi.gachipay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GachiPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GachiPayApplication.class, args);
    }

}
