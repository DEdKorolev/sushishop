package com.example.sushishop.controllers;

import com.example.sushishop.dto.ProductDto;
import com.example.sushishop.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    private final ProductService productService;


    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Long id){
        System.out.println("Вызван метод getById");
        return productService.getById(id);
    }

    @PostMapping
    public void addProduct(@RequestBody ProductDto dto){
        System.out.println("Вызван метод addProduct");
        productService.addProduct(dto);
    }

}
