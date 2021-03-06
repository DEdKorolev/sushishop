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
		System.out.println("Вызван метод addToUserBucket");
		User user = userService.findByName(username); // Поиск юзера
		if(user == null){
			throw new RuntimeException("Пользователь " + username + " не найден.");
		}

		// По пользователю ищем корзину
		Bucket bucket = user.getBucket();
		if(bucket == null){
			// Если корзины у пользователя нет, то создать
			Bucket newBucket = bucketService.createBucket(user, Collections.singletonList(productId));
			user.setBucket(newBucket); // Прикрепить корзину к пользователю
			userService.save(user);
		} else {
			bucketService.addProducts(bucket, Collections.singletonList(productId));
		}
	}

	@Override
	@Transactional
	public void addProduct(ProductDto dto) {
		System.out.println("Вызван метод addProduct");
		Product product = mapper.toProduct(dto);
		Product savedProduct = productRepository.save(product);

		template.convertAndSend("/topic/products",
				ProductMapper.MAPPER.fromProduct(savedProduct));
	}

	@Override
	public ProductDto getById(Long id) {
		System.out.println("Вызван метод getById");
		Product product = productRepository.findById(id).orElse(new Product());
		return ProductMapper.MAPPER.fromProduct(product);
	}

	@Override
	@Transactional
	public void updateUserBucket(String username) {
		System.out.println("Вызван метод updateUserBucket");
		template.convertAndSend("/topic/bucket", bucketService.getBucketByUser(username));
	}

	@Override
	public void deleteProduct(Long id) {
		System.out.println("Вызван метод deleteProduct");
		productRepository.deleteById(id);
	}

}
