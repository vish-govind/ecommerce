package com.ecommerce.dmart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.dmart.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findByCategory(String category);

}
