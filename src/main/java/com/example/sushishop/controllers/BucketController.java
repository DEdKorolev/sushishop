package com.example.sushishop.controllers;

import com.example.sushishop.domain.Bucket;
import com.example.sushishop.dto.BucketDto;
import com.example.sushishop.dto.BucketDetailDto;
import com.example.sushishop.service.BucketService;
import com.example.sushishop.service.ProductService;
import com.example.sushishop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

@Controller
public class BucketController {

	private final BucketService bucketService;
	private final UserService userService;
	private final ProductService productService;

	public BucketController(BucketService bucketService, UserService userService, ProductService productService) {
		this.bucketService = bucketService;
		this.userService = userService;
		this.productService = productService;
	}

	@GetMapping("/bucket")
	public String aboutBucket(Model model, Principal principal){
		if(principal == null){
			model.addAttribute("bucket", new BucketDto());
		}
		else {
			BucketDto bucketDto = bucketService.getBucketByUser(principal.getName());
			model.addAttribute("bucket", bucketDto);
		}

		return "bucket";
	}

	@PostMapping("/bucket")
	public String commitBucket(Principal principal){
		if(principal != null){
			bucketService.commitBucketToOrder(principal.getName());
		}
		return "redirect:/bucket";
	}

	@GetMapping("/bucket/{id}/delete")
	public String pageDeleteFromBucket(@PathVariable Long id, Principal principal) {
		if(principal != null) {
			bucketService.deleteProduct(userService.findByName(principal.getName()).getBucket(), id);
			productService.updateUserBucket(principal.getName());
		}
		return "redirect:/bucket";
	}

//	@GetMapping("/{id}/delete")
//	public String deleteProduct(@PathVariable Long id) {
//		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//		bucketService.deleteBucketProduct(id);
//		return "redirect:/bucket";
//	}

//	@GetMapping("/delete/{id}")
//	public String delBucket(@PathVariable Long id){
//		return "redirect:/bucket";
//	}

//	@RequestMapping("/{id}/bucket/delete_product")
//	public String deleteBucket(@PathVariable Long id, Principal principal) {
//		sessionObjectHolder.
//	}

//	@GetMapping("/deleteProduct")
//	public String deleteProduct(@RequestParam Long id) {
//		bucketService.deleteById(id);
//		return "redirect:/bucket";
//	}

//	@GetMapping("/deleteProduct")
//	public String deleteProduct(Principal principal) {
//		bucketService.deleteById(Long id);
//		return "redirect:/bucket";
//	}
}

