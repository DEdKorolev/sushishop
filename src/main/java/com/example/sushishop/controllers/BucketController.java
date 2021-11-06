package com.example.sushishop.controllers;

import com.example.sushishop.dto.BucketDto;
import com.example.sushishop.service.BucketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.security.Principal;

@Controller
public class BucketController {

	private final BucketService bucketService;

	public BucketController(BucketService bucketService) {
		this.bucketService = bucketService;
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

