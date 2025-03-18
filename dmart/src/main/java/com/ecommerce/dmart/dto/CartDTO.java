package com.ecommerce.dmart.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
	
	private Long cartId;   
    private Long userId; 
    private String userName; 
    private List<CartItemDTO> items;

}
