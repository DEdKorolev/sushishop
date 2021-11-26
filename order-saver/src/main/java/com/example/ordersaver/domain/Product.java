package com.example.ordersaver.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Product {
    private String id;
    private String title;
    private Double price;

    public Product(String title, Double price) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.price = price;
    }
}

