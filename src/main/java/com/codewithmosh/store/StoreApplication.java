package com.codewithmosh.store;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(servers = {
        @Server(url = "https://spring-boot-store-app-production.up.railway.app")
})
@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
       // System.out.println("🛠  PORT env var = '" + System.getenv("PORT") + "'");
        SpringApplication.run(StoreApplication.class, args);
    }
}
