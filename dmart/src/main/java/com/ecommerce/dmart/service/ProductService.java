package com.ecommerce.dmart.service;

import java.util.List;

import com.ecommerce.dmart.dto.ProductDTO;

public interface ProductService {
	
	ProductDTO addProduct(ProductDTO productDto);
	ProductDTO updateProduct(Long id, ProductDTO productDto);
	List<ProductDTO> getAllProducts();
	List<ProductDTO> getProductsByCategory(String category);
	void deleteProduct(Long id);
	ProductDTO getProductById(Long id);

}
