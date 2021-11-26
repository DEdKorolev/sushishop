package com.example.ordersaver.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;

@SpringBootApplication
public class SpringProductsClient {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringProductsClient.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));
        ConfigurableApplicationContext context = app.run(args);
    }
}
