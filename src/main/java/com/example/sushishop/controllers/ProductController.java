package com.example.sushishop.controllers;

import com.example.sushishop.dto.ProductDto;
import com.example.sushishop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products") // Работа с URL /products
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	// Во вьюху products передаем список продуктов
	@GetMapping
	public String list(Model model){
		System.out.println("Вызван метод list. Переход на вкладку \"Меню\".");
		List<ProductDto> list = productService.getAll();
		model.addAttribute("products", list);
		return "products";
	}

	// Метод, который добавляет продукты в корзину по id корзины
	@GetMapping("/{id}/bucket")
	public String addBucket(@PathVariable Long id, Principal principal){
		System.out.println("Вызван метод addBucket");
		if(principal == null){
			return "redirect:/products";
		}
		// К юзерю добавляем корзину
		productService.addToUserBucket(id, principal.getName());
		return "redirect:/products";
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public ResponseEntity<Void> addProduct(ProductDto dto){
		System.out.println("Вызван метод addProduct");
		productService.addProduct(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@MessageMapping("/products")
	public void messageAddProduct(ProductDto dto){
		System.out.println("Вызван метод messageAddProduct");
		productService.addProduct(dto);
	}

	@GetMapping("/{id}")
	@ResponseBody
	public ProductDto getById(@PathVariable Long id){
		System.out.println("Вызван метод getById");
		return productService.getById(id);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/{id}/delete")
	public String deleteProductFromMenu(@PathVariable Long id) {
		System.out.println("Вызван метод deleteProductFromMenu");
		productService.deleteProduct(id);
		return "redirect:/products";
	}
}
