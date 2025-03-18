package com.ecommerce.dmart.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ecommerce.dmart.dto.ProductDTO;
import com.ecommerce.dmart.exception.EntityNotFoundException;
import com.ecommerce.dmart.mapper.ProductMapper;
import com.ecommerce.dmart.model.Product;
import com.ecommerce.dmart.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	private final ProductRepository productRepo;

	public ProductServiceImpl(ProductRepository productRepo) {
		this.productRepo = productRepo;
	}

	@Override
	@CacheEvict(value = "products", allEntries = true)
	public ProductDTO addProduct(ProductDTO productDto) {
		logger.info("Adding new product: {}", productDto.getProductName());
		Product product = ProductMapper.toEntity(productDto);
		Product savedProduct = productRepo.save(product);
		logger.info("Product added successfully with ID: {}", savedProduct.getProductId());
		return ProductMapper.toDTO(savedProduct);
	}

	@Override
	@CachePut(value = "products", key = "#id")
	@CacheEvict(value = "allProducts", allEntries = true)
	public ProductDTO updateProduct(Long id, ProductDTO productDto) {
		logger.info("Updating product with ID: {}", id);
		
		Product product = productRepo.findById(id).orElseThrow(() -> {
		    logger.error("Product with ID {} not found", id);
		    return new EntityNotFoundException("Product", id);
		});

		product.setProductName(productDto.getProductName());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setCategory(productDto.getCategory());
		product.setImageUrl(productDto.getImageUrl());

		Product updatedProduct = productRepo.save(product);
		logger.info("Product updated successfully with ID: {}", updatedProduct.getProductId());
		return ProductMapper.toDTO(updatedProduct);
	}

	@Override
	@Cacheable(value = "allProducts")
	public List<ProductDTO> getAllProducts() {
		logger.info("Fetching all products");
		return productRepo.findAll().stream().map(product -> ProductMapper.toDTO(product)).collect(Collectors.toList());
	}

	@Override
	@Cacheable(value = "productsByCategory", key = "#category")
	public List<ProductDTO> getProductsByCategory(String category) {
		logger.info("Fetching products for category: {}", category);
		List<ProductDTO> products = productRepo.findByCategory(category).stream()
				.map(product -> ProductMapper.toDTO(product)).collect(Collectors.toList());

		if (products.isEmpty()) {
			logger.warn("No products found for category: {}", category);
			throw new EntityNotFoundException("No products found for the category" + category);
		}

		return products;
	}

	@Override
	@Cacheable(value = "products", key = "#id") // Cache individual product lookups
	public ProductDTO getProductById(Long id) {
		logger.info("Fetching product with ID: {}", id);
		
		Product product = productRepo.findById(id).orElseThrow(() -> {
		    logger.error("Product with ID {} not found", id);
		    return new EntityNotFoundException("Product", id);
		});
		return ProductMapper.toDTO(product);
	}

	@Override
	@CacheEvict(value = { "products", "allProducts", "productsByCategory" }, allEntries = true)
	public void deleteProduct(Long id) {
		logger.info("Deleting product with ID: {}", id);
		if (!productRepo.existsById(id)) {
			logger.warn("Product with ID: {} not found", id);
			throw new EntityNotFoundException("Product", id);
		}
		productRepo.deleteById(id);
		logger.info("Product deleted successfully with ID: {}", id);
	}
}
