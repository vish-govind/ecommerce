package com.ecommerce.dmart.service;

import java.util.List;

import com.ecommerce.dmart.dto.CartDTO;
import com.ecommerce.dmart.dto.CartItemDTO;

public interface CartService {

    CartDTO addToCart(String username, CartItemDTO cartItemDto);

    void updateCart(String username, CartItemDTO cartItemDto);

    void removeFromCart(String username, Long productId);

    List<CartItemDTO> getCartItems(String username);

    void clearCart(String username);

    CartDTO getCart(String username);
}
