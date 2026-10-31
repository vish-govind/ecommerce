package com.ecommerce.dmart.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    private static final Logger logger =
            LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<CartDTO> addToCart(
            Authentication authentication,
            @RequestBody CartItemDTO cartItemDto) {

        String username = authentication.getName();
        logger.info("Adding product {} to cart for user {}",
                cartItemDto.getProductId(), username);

        CartDTO updatedCart = cartService.addToCart(username, cartItemDto);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/items")
    public ResponseEntity<String> updateCart(
            Authentication authentication,
            @RequestBody CartItemDTO cartItemDto) {

        String username = authentication.getName();
        cartService.updateCart(username, cartItemDto);
        return ResponseEntity.ok("Cart updated");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeFromCart(
            Authentication authentication,
            @PathVariable Long productId) {

        String username = authentication.getName();
        cartService.removeFromCart(username, productId);
        return ResponseEntity.ok("Item removed from cart");
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItemDTO>> getCartItems(
            Authentication authentication) {

        String username = authentication.getName();
        return ResponseEntity.ok(cartService.getCartItems(username));
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart(Authentication authentication) {

        String username = authentication.getName();
        cartService.clearCart(username);
        return ResponseEntity.ok("Cleared the Cart");
    }

    @GetMapping
    public ResponseEntity<CartDTO> getCart(Authentication authentication) {

        String username = authentication.getName();
        return ResponseEntity.ok(cartService.getCart(username));
    }
}
