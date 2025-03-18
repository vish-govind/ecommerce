package com.ecommerce.dmart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.dmart.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	
	List<Order> findByUserUserId(Long userId);

}
