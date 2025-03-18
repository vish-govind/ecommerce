package com.ecommerce.dmart.service;

import java.util.List;

import com.ecommerce.dmart.dto.CartDTO;
import com.ecommerce.dmart.dto.CartItemDTO;

public interface CartService  {
	
	CartDTO addToCart(Long userId, CartItemDTO cartItemDto);
    void updateCart(Long userId, CartItemDTO cartItemDto);
    void removeFromCart(Long userId,Long productId);
    List<CartItemDTO> getCartItems(Long userId);
    void clearCart(Long userId);
	CartDTO getCart(Long userId);

}
