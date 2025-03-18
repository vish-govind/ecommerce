package com.ecommerce.dmart.mapper;

import java.util.stream.Collectors;

import com.ecommerce.dmart.dto.CartDTO;
import com.ecommerce.dmart.dto.CartItemDTO;
import com.ecommerce.dmart.model.Cart;

public class CartMapper {
	
	 public static CartDTO toDTO(Cart cart) {
	        return new CartDTO(
	                cart.getCartId(),
	                cart.getUser().getUserId(),
	                cart.getUser().getUserName(),
	                cart.getItems().stream()
	                        .map(item -> new CartItemDTO(
	                                item.getCartItemid(),
	                                item.getCart().getCartId(),
	                                item.getProduct().getProductId(),
	                                item.getProduct().getProductName(),
	                                item.getQuantity()
	                        ))
	                        .collect(Collectors.toList())
	        );
	    }
}
