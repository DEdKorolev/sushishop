package com.example.sushishop.service;

import com.example.sushishop.dao.ProductRepository;
import com.example.sushishop.domain.Bucket;
import com.example.sushishop.domain.Product;
import com.example.sushishop.domain.User;
import com.example.sushishop.dto.ProductDto;
import com.example.sushishop.mapper.ProductMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductMapper mapper = ProductMapper.MAPPER;

	private final ProductRepository productRepository;
	private final UserService userService;
	private final BucketService bucketService;
	private final SimpMessagingTemplate template;

	public ProductServiceImpl(ProductRepository productRepository,
							  UserService userService,
							  BucketService bucketService,
							  SimpMessagingTemplate template) {
		this.productRepository = productRepository;
		this.userService = userService;
		this.bucketService = bucketService;
		this.template = template;
	}

	@Override
	public List<ProductDto> getAll() {
		return mapper.fromProductList(productRepository.findAll());
	}

	@Override
	@Transactional
	public void addToUserBucket(Long productId, String username) {
		User user = userService.findByName(username); // Поиск юзера
		if(user == null){
			throw new RuntimeException("Пользователь " + username + " не найден.");
		}

		// По юзеру ищем корзину
		Bucket bucket = user.getBucket();
		if(bucket == null){
			// Если корзины у юзера нет, то создать
			Bucket newBucket = bucketService.createBucket(user, Collections.singletonList(productId));
			user.setBucket(newBucket); // Прикрепить корзину к юзеру
			userService.save(user);
		}
		else {
			bucketService.addProducts(bucket, Collections.singletonList(productId));
		}
	}

	@Override
	@Transactional
	public void addProduct(ProductDto dto) {
		Product product = mapper.toProduct(dto);
		Product savedProduct = productRepository.save(product);

		template.convertAndSend("/topic/products",
				ProductMapper.MAPPER.fromProduct(savedProduct));
	}

	@Override
	public ProductDto getById(Long id) {
		Product product = productRepository.findById(id).orElse(new Product());
		return ProductMapper.MAPPER.fromProduct(product);
	}

	@Override
	@Transactional
	public void updateUserBucket(String username) {
		template.convertAndSend("/topic/bucket", bucketService.getBucketByUser(username));
	}

	@Override
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}

}
