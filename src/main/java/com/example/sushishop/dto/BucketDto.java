package com.example.sushishop.dto;

import com.example.sushishop.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketDto {
	private long id;
	private long amountProducts;
	private double sum;
	private List<BucketDetailDto> bucketDetails = new ArrayList<>();

	// Для подсчёта количества товара в корзине
	public void aggregate(){
		this.amountProducts = bucketDetails.size();
		this.sum = bucketDetails.stream()
				.map(BucketDetailDto::getSum)
				.mapToDouble(Double::doubleValue)
				.sum();
	}
}
