package com.ecommerce.dmart.mapper;

import com.ecommerce.dmart.dto.ProductDTO;
import com.ecommerce.dmart.model.Product;

public class ProductMapper {
	
	  public static ProductDTO toDTO(Product product) {
	        return new ProductDTO(
	                product.getProductId(),
	                product.getProductName(),
	                product.getDescription(),
	                product.getPrice(),
	                product.getCategory(),
	                product.getImageUrl()
	        );
	    }
	  
	  public static Product toEntity(ProductDTO productDto) {
	        return new Product(
	                null,  // ID is set automatically when saving to DB
	                productDto.getProductName(),
	                productDto.getDescription(),
	                productDto.getPrice(),
	                productDto.getCategory(),
	                productDto.getImageUrl()
	        );
	    }

}
