package com.ecommerce.dmart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.dmart.model.Cart;
import com.ecommerce.dmart.model.CartItem;
import com.ecommerce.dmart.model.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	
	List<CartItem> findByCart(Cart cart);
	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}
