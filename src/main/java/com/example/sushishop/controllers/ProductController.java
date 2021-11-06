package com.example.sushishop.controllers;

import com.example.sushishop.dto.ProductDto;
import com.example.sushishop.service.ProductService;
import com.example.sushishop.service.SessionObjectHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	private final SessionObjectHolder sessionObjectHolder;

	public ProductController(ProductService productService, SessionObjectHolder sessionObjectHolder) {
		this.productService = productService;
		this.sessionObjectHolder = sessionObjectHolder;
	}

	@GetMapping
	public String list(Model model){
		sessionObjectHolder.addClick();
		List<ProductDto> list = productService.getAll();
		model.addAttribute("products", list);
		return "products";
	}

	// Метод, который добавляет продукты в корзину по id корзины
	@GetMapping("/{id}/bucket")
	public String addBucket(@PathVariable Long id, Principal principal){
		sessionObjectHolder.addClick();
		if(principal == null){
			return "redirect:/products";
		}
		productService.addToUserBucket(id, principal.getName());
		return "redirect:/products";
	}

	@PostMapping
	public ResponseEntity<Void> addProduct(ProductDto dto){
		productService.addProduct(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@MessageMapping("/products")
	public void messageAddProduct(ProductDto dto){
		productService.addProduct(dto);
	}

	@GetMapping("/{id}")
	@ResponseBody
	public ProductDto getById(@PathVariable Long id){
		return productService.getById(id);
	}

}
