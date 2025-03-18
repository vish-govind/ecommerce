package com.ecommerce.dmart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

	private Long cartItemId;
	private Long cartId;
	private Long productId;
	private String productName;
	private int quantity;
}
