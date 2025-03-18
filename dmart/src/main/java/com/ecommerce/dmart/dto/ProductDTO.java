package com.ecommerce.dmart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	
	private Long productId;

    private String productName;
    private String description;
    private double price;
    private String category;
    private String imageUrl;
   
}
