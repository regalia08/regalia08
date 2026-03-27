package com.example.regalia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class RegaliaApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegaliaApplication.class, args);
    }
}

