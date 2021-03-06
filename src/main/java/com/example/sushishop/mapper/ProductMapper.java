package com.example.sushishop.mapper;

import com.example.sushishop.domain.Product;
import com.example.sushishop.dto.ProductDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
	ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

	Product toProduct(ProductDto dto);

	@InheritInverseConfiguration
	ProductDto fromProduct(Product product);

	List<ProductDto> fromProductList(List<Product> products);

}