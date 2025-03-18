package com.ecommerce.dmart.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dmart.dto.OrderDTO;
import com.ecommerce.dmart.service.OrderService;

@RequestMapping("/orders")
@RestController
public class OrderController {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	 private final OrderService orderService;

	    public OrderController(OrderService orderService) {
	        this.orderService = orderService;
	    }

	    @PostMapping("/place/{userId}")
	    public ResponseEntity<OrderDTO> placeOrder(@PathVariable Long userId) {
	    	 logger.info("Placing order for user {}", userId);
	         OrderDTO order = orderService.placeOrder(userId);
	         logger.info("Order placed successfully for user {}", userId);
	         return ResponseEntity.ok(order);
	    }

	    @GetMapping("/user/{userId}")
	    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long userId) {
	    	 logger.info("Fetching orders for user {}", userId);
	         return ResponseEntity.ok(orderService.getOrdersByUser(userId));
	    }

	    @GetMapping("/{orderId}")
	    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
	    	 logger.info("Fetching order details for order {}", orderId);
	         return ResponseEntity.ok(orderService.getOrderById(orderId));
	    }

	    @PutMapping("/cancel/{orderId}")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
	    	logger.info("Admin attempting to cancel order {}", orderId);
	        orderService.cancelOrder(orderId);
	        logger.info("Order {} cancelled successfully", orderId);
	        return ResponseEntity.ok("Order cancelled successfully");
	    }

}
