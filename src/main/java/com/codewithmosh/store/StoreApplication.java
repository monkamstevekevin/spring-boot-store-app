package com.codewithmosh.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
        System.out.println("🛠  PORT env var = '" + System.getenv("PORT") + "'");
        SpringApplication.run(StoreApplication.class, args);
    }
}
