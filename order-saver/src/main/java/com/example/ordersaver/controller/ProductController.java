package com.example.ordersaver.controller;

import com.example.ordersaver.domain.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    @Value("${server.port:8080}")
    private int port;

    private static List<Product> products = new ArrayList<>();

    static {
        products.add(new Product("Milk", 56.66));
        products.add(new Product("Beer", 66.66));
        products.add(new Product("Chocolate", 116.00));
        products.add(new Product("Bread", 36.00));
    }

    @GetMapping
    public List<Product> getProducts(){
        System.out.println("Server port -> " + port);
        return products;
    }

}
