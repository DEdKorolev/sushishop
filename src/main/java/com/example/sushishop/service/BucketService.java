package com.example.sushishop.service;

import com.example.sushishop.domain.Bucket;
import com.example.sushishop.domain.User;
import com.example.sushishop.dto.BucketDto;

import java.util.List;

public interface BucketService {
	Bucket createBucket(User user, List<Long> productIds);

	void addProducts(Bucket bucket, List<Long> productIds);

	BucketDto getBucketByUser(String name); // Поиск корзины по пользователю

	void commitBucketToOrder(String username);

//	void deleteById(Long id);
}
