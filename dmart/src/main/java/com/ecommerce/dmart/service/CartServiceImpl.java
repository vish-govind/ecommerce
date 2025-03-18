package com.ecommerce.dmart.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ecommerce.dmart.dto.CartDTO;
import com.ecommerce.dmart.dto.CartItemDTO;
import com.ecommerce.dmart.exception.EntityNotFoundException;
import com.ecommerce.dmart.exception.InvalidRequestException;
import com.ecommerce.dmart.mapper.CartMapper;
import com.ecommerce.dmart.model.Cart;
import com.ecommerce.dmart.model.CartItem;
import com.ecommerce.dmart.model.Product;
import com.ecommerce.dmart.model.User;
import com.ecommerce.dmart.repository.CartItemRepository;
import com.ecommerce.dmart.repository.CartRepository;
import com.ecommerce.dmart.repository.ProductRepository;
import com.ecommerce.dmart.repository.UserRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@Service
public class CartServiceImpl implements CartService{

	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	
	

	public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
			ProductRepository productRepository, UserRepository userRepository) {
		super();
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}

	  @Override
	    @CacheEvict(value = {"cart", "cartItems"}, key = "#userId")
	    public CartDTO addToCart(Long userId, CartItemDTO cartItemDto) {
	        try {
	            logger.info("Adding product {} to cart for user {}", cartItemDto.getProductId(), userId);

	            if (cartItemDto.getProductId() == null || cartItemDto.getQuantity() <= 0) {
	                logger.error("Invalid product ID {} or quantity {} for user {}", cartItemDto.getProductId(), cartItemDto.getQuantity(), userId);
	                throw new InvalidRequestException("Invalid product ID or quantity");
	            }

	            User user = userRepository.findById(userId)
	                    .orElseThrow(() -> new EntityNotFoundException("User", userId));

	            Cart cart = cartRepository.findByUser(user)
	                    .orElseGet(() -> {
	                        Cart newCart = new Cart();
	                        newCart.setUser(user);
	                        return cartRepository.save(newCart);
	                    });

	            Product product = productRepository.findById(cartItemDto.getProductId())
	                    .orElseThrow(() -> new EntityNotFoundException("Product", cartItemDto.getProductId()));

	            Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);

	            if (existingCartItem.isPresent()) {
	                CartItem cartItem = existingCartItem.get();
	                cartItem.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
	                cartItemRepository.save(cartItem);
	            } else {
	                CartItem newCartItem = new CartItem();
	                newCartItem.setCart(cart);
	                newCartItem.setProduct(product);
	                newCartItem.setQuantity(cartItemDto.getQuantity());
	                cartItemRepository.save(newCartItem);
	            }

	            Cart updatedCart = cartRepository.findById(cart.getCartId())
	                    .orElseThrow(() -> new EntityNotFoundException("Cart", cart.getCartId()));

	            logger.info("Product {} added to cart for user {}", cartItemDto.getProductId(), userId);
	            return CartMapper.toDTO(updatedCart);
	        } catch (Exception e) {
	            logger.error("Error adding product {} to cart for user {}: {}", cartItemDto.getProductId(), userId, e.getMessage(), e);
	            throw e;
	        }
	    }

	    @Override
	    @CacheEvict(value = {"cart", "cartItems"}, key = "#userId")
	    public void updateCart(Long userId, CartItemDTO cartItemDto) {
	        try {
	            logger.info("Updating cart for user {} with product {}", userId, cartItemDto.getProductId());

	            if (cartItemDto.getQuantity() <= 0) {
	                logger.error("Invalid quantity {} for product {} in user {}'s cart", cartItemDto.getQuantity(), cartItemDto.getProductId(), userId);
	                throw new InvalidRequestException("Quantity must be greater than zero");
	            }

	            User user = userRepository.findById(userId)
	                    .orElseThrow(() -> new EntityNotFoundException("User", userId));

	            Cart cart = cartRepository.findByUser(user)
	                    .orElseThrow(() -> new EntityNotFoundException("No cart found for user " + userId));

	            Product product = productRepository.findById(cartItemDto.getProductId())
	                    .orElseThrow(() -> new EntityNotFoundException("Product", cartItemDto.getProductId()));

	            CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
	                    .orElseThrow(() -> new EntityNotFoundException("No cart item found for product " + cartItemDto.getProductId() + " in user's cart"));

	            cartItem.setQuantity(cartItemDto.getQuantity());
	            cartItemRepository.save(cartItem);
	            logger.info("Cart updated for user {} with product {}", userId, cartItemDto.getProductId());
	        } catch (Exception e) {
	            logger.error("Error updating cart for user {} with product {}: {}", userId, cartItemDto.getProductId(), e.getMessage(), e);
	            throw e;
	        }
	    }

	    @Override
	    @CacheEvict(value = {"cart", "cartItems"}, key = "#userId")
	    public void removeFromCart(Long userId, Long productId) {
	        try {
	            logger.info("Removing product {} from cart for user {}", productId, userId);
	            
	            User user = userRepository.findById(userId)
	                    .orElseThrow(() -> new EntityNotFoundException("User", userId));
	            
	            Cart cart = cartRepository.findByUser(user)
	                    .orElseThrow(() -> new EntityNotFoundException("Cart not found for userId " + userId));
	            
	            Product product = productRepository.findById(productId)
	                    .orElseThrow(() -> new EntityNotFoundException("Product", productId));
	            
	            CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
	                    .orElseThrow(() -> new EntityNotFoundException("Cart item with productId " + productId + " was not found"));
	            
	            cartItemRepository.delete(cartItem);
	            logger.info("Product {} removed from cart for user {}", productId, userId);
	        } catch (Exception e) {
	            logger.error("Error removing product {} from cart for user {}: {}", productId, userId, e.getMessage(), e);
	            throw e;
	        }
	    }
	


		@Override
		@Cacheable(value = "cartItems", key = "#userId")
		@CircuitBreaker(name = "cartService", fallbackMethod = "fallbackGetCartItems")
		public List<CartItemDTO> getCartItems(Long userId) {
			logger.info("Fetching cart items for user {}", userId);
			try {
				User user = userRepository.findById(userId)
						.orElseThrow(() -> new EntityNotFoundException("User", userId));

				Cart cart = cartRepository.findByUser(user)
						.orElseThrow(() -> new EntityNotFoundException("Cart not found for userId " + userId));

				logger.info("Cart items fetched for user {}", userId);
				return cart.getItems().stream()
						.map(item -> new CartItemDTO(
								item.getCartItemid(),
								item.getCart().getCartId(),
								item.getProduct().getProductId(),
								item.getProduct().getProductName(),
								item.getQuantity()
						))
						.collect(Collectors.toList());
			} catch (Exception e) {
				logger.error("Error fetching cart items for user {}: {}", userId, e.getMessage());
				throw e;
			}
		}
		
		public List<CartItemDTO> fallbackGetCartItems(Long userId, Throwable t) {
		    logger.warn("Circuit breaker triggered for getCartItems(), returning empty list. Reason: {}", t.getMessage());
		 // Returning an empty list instead of throwing an exception
		    return List.of();  
		}

		@Override
		@CacheEvict(value = {"cart", "cartItems"}, key = "#userId")
		public void clearCart(Long userId) {
			logger.info("Clearing cart for user {}", userId);
			try {
				User user = userRepository.findById(userId)
						.orElseThrow(() -> new EntityNotFoundException("User", userId));

				Cart cart = cartRepository.findByUser(user)
						.orElseThrow(() -> new EntityNotFoundException("Cart not found for userId " + userId));

				cartItemRepository.deleteAll(cart.getItems());
				logger.info("Cart cleared for user {}", userId);
			} catch (Exception e) {
				logger.error("Error clearing cart for user {}: {}", userId, e.getMessage());
				throw e;
			}
		}
		
		@Override
		@Cacheable(value = "cart", key = "#userId")
		@CircuitBreaker(name = "cartService", fallbackMethod = "fallbackGetCart")
		public CartDTO getCart(Long userId) {
			logger.info("Fetching cart for user {}", userId);
			try {
				User user = userRepository.findById(userId)
						.orElseThrow(() -> new EntityNotFoundException("User", userId));

				Cart cart = cartRepository.findByUser(user)
						.orElseThrow(() -> new EntityNotFoundException("Cart not found for userId " + userId));

				logger.info("Cart fetched for user {}", userId);
				return CartMapper.toDTO(cart);
			} catch (Exception e) {
				logger.error("Error fetching cart for user {}: {}", userId, e.getMessage());
				throw e;
			}
		}
		
		public CartDTO fallbackGetCart(Long userId, Throwable t) {
		    logger.warn("Circuit breaker triggered for getCart(), returning empty cart. Reason: {}", t.getMessage());
		 // Return an empty cart
		    return new CartDTO(null, userId, "Unknown", List.of());
		}
}

