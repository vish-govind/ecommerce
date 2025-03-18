package com.ecommerce.dmart.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dmart.dto.CartDTO;
import com.ecommerce.dmart.dto.CartItemDTO;
import com.ecommerce.dmart.service.CartService;


@RestController
@RequestMapping("/cart")
public class CartController {
	
	 private static final Logger logger = LoggerFactory.getLogger(CartController.class);	
	 private final CartService cartService;

	    public CartController(CartService cartService) {
	        this.cartService = cartService;
	    }

	    @PostMapping("/add/{userId}")
	    public ResponseEntity<CartDTO> addToCart(@PathVariable Long userId, @RequestBody CartItemDTO cartItemDto) {
	    	logger.info("Adding product {} to cart for user {}", cartItemDto.getProductId(), userId);
	        CartDTO updatedCart = cartService.addToCart(userId, cartItemDto);
	        logger.info("Successfully added product {} to cart for user {}", cartItemDto.getProductId(), userId);
	        return ResponseEntity.ok(updatedCart);
	    }

	    @PutMapping("/update/{userId}")
	    public ResponseEntity<String> updateCart(@PathVariable Long userId, @RequestBody CartItemDTO cartItemDto) {
	    	logger.info("Updating cart for user {} with product {}", userId, cartItemDto.getProductId());
	        cartService.updateCart(userId, cartItemDto);
	        logger.info("Cart updated successfully for user {}", userId);
	        return ResponseEntity.ok("Cart updated");
	    }

	    @DeleteMapping("/remove/{userId}/{productId}")
	    public ResponseEntity<String> removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
	    	logger.info("Removing product {} from cart of user {}", productId, userId);
	        cartService.removeFromCart(userId, productId);
	        logger.info("Product {} removed from cart for user {}", productId, userId);
	        return ResponseEntity.ok("Item removed from cart");
	    }

	    @GetMapping("/items/{userId}")
	    public ResponseEntity<List<CartItemDTO>> getCartItems(@PathVariable Long userId) {
	    	logger.info("Fetching cart items for user {}", userId);
	        return ResponseEntity.ok(cartService.getCartItems(userId));
	    }
	    
	    @DeleteMapping("/remove/{userId}")
	    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
	    	logger.info("Clearing cart for user {}", userId);
	        cartService.clearCart(userId);
	        logger.info("Cart cleared successfully for user {}", userId);
	        return ResponseEntity.ok("Cleared the Cart");
	    }


}
