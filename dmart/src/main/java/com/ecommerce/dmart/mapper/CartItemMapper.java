package com.ecommerce.dmart.mapper;

import com.ecommerce.dmart.dto.CartItemDTO;
import com.ecommerce.dmart.model.CartItem;

public class CartItemMapper {
	
	  public static CartItemDTO toDTO(CartItem cartItem) {
	        return new CartItemDTO(
	        		cartItem.getCartItemid(),
	        		cartItem.getCart().getCartId(),
	        		cartItem.getProduct().getProductId(),
	        		cartItem.getProduct().getProductName(),
	        		cartItem.getQuantity());
	    }	
}
