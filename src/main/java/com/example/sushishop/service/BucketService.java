package com.example.sushishop.service;

import com.example.sushishop.domain.Bucket;
import com.example.sushishop.domain.User;
import com.example.sushishop.dto.BucketDto;

import java.util.List;

public interface BucketService {
	// Создание корзины по определенному юзеру
	Bucket createBucket(User user, List<Long> productIds);
	// Добавление продуктов в корзину
	void addProducts(Bucket bucket, List<Long> productIds);

	BucketDto getBucketByUser(String name); // Поиск корзины по пользователю

	void commitBucketToOrder(String username);

	void deleteProduct(Bucket bucket, Long productId);
//    void deleteBucketProduct(Long id);

//	long getBucket(Long id);
}
