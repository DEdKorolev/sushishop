package com.example.sushishop.service;

import com.example.sushishop.dao.BucketRepository;
import com.example.sushishop.dao.ProductRepository;
import com.example.sushishop.domain.*;
import com.example.sushishop.dto.BucketDto;
import com.example.sushishop.dto.BucketDetailDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BucketServiceImpl implements BucketService {

	private final BucketRepository bucketRepository;
	private final ProductRepository productRepository;
	private final UserService userService;
	private final OrderService orderService;

	public BucketServiceImpl(BucketRepository bucketRepository,
							 ProductRepository productRepository,
							 UserService userService,
							 OrderService orderService) {
		this.bucketRepository = bucketRepository;
		this.productRepository = productRepository;
		this.userService = userService;
		this.orderService = orderService;
	}

	// Создание корзины
	@Override
	@Transactional
	public Bucket createBucket(User user, List<Long> productIds) {
		System.out.println("Вызван метод createBucket");
		Bucket bucket = new Bucket();
		bucket.setUser(user); // Добавление к корзине юзера
		// Список продуктов в корзине данного пользователя
		List<Product> productList = getCollectRefProductsByIds(productIds);
		bucket.setProducts(productList);
		return bucketRepository.save(bucket);
	}

	// Формирования списка продуктов
	private List<Product> getCollectRefProductsByIds(List<Long> productIds) {
		return productIds.stream()
				// getOne вытаскивает ссылку на объект и обращается к начинке объекта, findByID вытаскивает сам объект (использовать избыточно)
				.map(productRepository::getOne)
				.collect(Collectors.toList());
	}

	// Добавление продуктов в корзину по ID
	@Override
	@Transactional
	public void addProducts(Bucket bucket, List<Long> productIds) {
		System.out.println("Вызван метод addProducts");
		List<Product> products = bucket.getProducts();
		List<Product> newProductsList = products == null ? new ArrayList<>() : new ArrayList<>(products);
		newProductsList.addAll(getCollectRefProductsByIds(productIds));
		bucket.setProducts(newProductsList); // Список продуктов добавляем в корзину
		System.out.println("Продукт" + getCollectRefProductsByIds(productIds) + "добален в корзину");
		bucketRepository.save(bucket); // Сохранение корзины в репозиторий
	}

	//Метод подсчёта количества товара в корзине.
	@Override
	public BucketDto getBucketByUser(String name) {
		System.out.println("Вызван метод getBucketByUser");
		User user = userService.findByName(name);
		if(user == null || user.getBucket() == null){
			return new BucketDto();
		}

		BucketDto bucketDto = new BucketDto();
		Map<Long, BucketDetailDto> mapByProductId = new HashMap<>();

		List<Product> products = user.getBucket().getProducts();
		for (Product product : products) {
			BucketDetailDto detail = mapByProductId.get(product.getId());
			if(detail == null){
				mapByProductId.put(product.getId(), new BucketDetailDto(product));
			}
			else {
				detail.setAmount(detail.getAmount() + 1.0);
				detail.setSum(detail.getSum() + product.getPrice());
			}
		}

		bucketDto.setBucketDetails(new ArrayList<>(mapByProductId.values()));
		bucketDto.aggregate();

		return bucketDto;
	}

	@Override
	@Transactional
	public void commitBucketToOrder(String username) {
		System.out.println("Вызван метод commitBucketToOrder");
		User user = userService.findByName(username);
		if(user == null){
			throw new RuntimeException("Пользователь не найден");
		}
		Bucket bucket = user.getBucket();
		if(bucket == null || bucket.getProducts().isEmpty()){
			return;
		}

		Order order = new Order();
		order.setStatus(OrderStatus.NEW);
		order.setUser(user);

		Map<Product, Long> productWithAmount = bucket.getProducts().stream()
				.collect(Collectors.groupingBy(product -> product, Collectors.counting()));

		List<OrderDetails> orderDetails = productWithAmount.entrySet().stream()
				.map(pair -> new OrderDetails(order, pair.getKey(), pair.getValue()))
				.collect(Collectors.toList());

		BigDecimal total = new BigDecimal(orderDetails.stream()
				.map(detail -> detail.getPrice().multiply(detail.getAmount()))
				.mapToDouble(BigDecimal::doubleValue).sum());

		order.setDetails(orderDetails);
		order.setSum(total);
		order.setAddress(user.getAddress());

		orderService.saveOrder(order);
		bucket.getProducts().clear();
		bucketRepository.save(bucket);
	}

	@Override
	public void deleteProduct(Bucket bucket, Long productId) {
		System.out.println("Вызван метод deleteProduct");
		List<Product> products = bucket.getProducts();
		products.remove(products.stream()
				.filter(product -> productId.equals(product.getId()))
				.findAny()
				.orElse(null));
		bucket.setProducts(products);
		bucketRepository.save(bucket);
	}
}