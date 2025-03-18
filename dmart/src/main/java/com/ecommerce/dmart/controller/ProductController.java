package com.ecommerce.dmart.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dmart.dto.ProductDTO;
import com.ecommerce.dmart.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	private final ProductService productService;

	public ProductController(ProductService productService) {
		super();
		this.productService = productService;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDto) {
		 logger.info("Adding new product: {}", productDto.getProductName());
	     return ResponseEntity.ok(productService.addProduct(productDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDto) {
    	logger.info("Updating product {} with new details", id);
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
    	  logger.info("Deleting product {}", id);
          productService.deleteProduct(id);
          logger.info("Product {} deleted successfully", id);
          return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
    	logger.info("Fetching all available products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable String category) {
    	logger.info("Fetching all products for category" + category);
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }
	

}
