package com.ecommerce.dmart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.dmart.model.Cart;
import com.ecommerce.dmart.model.User;

public interface CartRepository extends JpaRepository<Cart, Long>{
	
	Optional<Cart> findByUser(User user);

}
