package com.example.sushishop.service;

import com.example.sushishop.domain.Product;
import com.example.sushishop.dto.ProductDto;

import java.util.List;

public interface ProductService {
	List<ProductDto> getAll();
	// Добавление продукта по ID к определенному пользователю
	void addToUserBucket(Long productId, String username);
	void addProduct(ProductDto dto);
	ProductDto getById(Long id);
	void updateUserBucket(String username);
	void deleteProduct(Long id);
}
