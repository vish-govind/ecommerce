package com.ecommerce.dmart.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ecommerce.dmart.dto.OrderDTO;
import com.ecommerce.dmart.exception.EntityNotFoundException;
import com.ecommerce.dmart.exception.InvalidRequestException;
import com.ecommerce.dmart.mapper.OrderMapper;
import com.ecommerce.dmart.model.Cart;
import com.ecommerce.dmart.model.Order;
import com.ecommerce.dmart.model.OrderStatus;
import com.ecommerce.dmart.model.User;
import com.ecommerce.dmart.repository.CartRepository;
import com.ecommerce.dmart.repository.OrderRepository;
import com.ecommerce.dmart.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService  {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, 
    		CartRepository cartRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"orders", "order"}, key = "#userId") 
    public OrderDTO placeOrder(Long userId) {
    	logger.info("Placing order for user {}", userId);
    	User user = userRepository.findById(userId).orElseThrow(() -> {
    	    logger.error("User with ID {} not found", userId);
    	    return new EntityNotFoundException("User", userId);
    	});

    	Cart cart = cartRepository.findByUser(user).orElseThrow(() -> {
    	    logger.error("No cart found for user with ID {}", userId);
    	    return new EntityNotFoundException("No cart found for user with ID " + userId);
    	});

    	if (cart.getItems().isEmpty()) {
    	    logger.error("Cannot place an order with an empty cart for user ID {}", userId);
    	    throw new InvalidRequestException("Cannot place an order with an empty cart.");
    	}

    	if (cart.getTotalAmount() <= 0) {
    	    logger.error("Order total amount must be greater than zero for user ID {}", userId);
    	    throw new InvalidRequestException("Order total amount must be greater than zero.");
    	}

        // Create order but DO NOT clear the cart yet
        Order order = new Order();
        order.setUser(user);
        order.setCart(cart);
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus(OrderStatus.PENDING);  // Order is pending until payment is confirmed
        order.setOrderDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        logger.info("Order placed successfully for user {} with order ID {}", userId, savedOrder.getOrderId());
        return OrderMapper.toDto(savedOrder);
    }

    @Override
    @Cacheable(value = "orders", key = "#userId")
    public List<OrderDTO> getOrdersByUser(Long userId) {
    	logger.info("Fetching orders for user {}", userId);
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User", userId);
        }

        List<Order> userOrders = orderRepository.findByUserUserId(userId);
        
        if (userOrders.isEmpty()) {
        	logger.error("Orders not found for userId {}", userId);
            throw new EntityNotFoundException("Orders for user ID", userId);
        }

        List<OrderDTO> orders = userOrders.stream()
                .map(order -> OrderMapper.toDto(order))
                .collect(Collectors.toList());
        logger.info("Fetched {} orders for user {}", orders.size(), userId);
        return orders;
    }


    @Override
    @Cacheable(value = "order", key = "#orderId") 
    public OrderDTO getOrderById(Long orderId) {
    	logger.info("Fetching order with ID {}", orderId);
    	 Order order = orderRepository.findById(orderId)
    	            .orElseThrow(() -> {
    	                logger.error("Order with ID {} not found", orderId);
    	                return new EntityNotFoundException("Order", orderId);
    	            });
        logger.info("Fetched order with ID {} successfully", orderId);
        return OrderMapper.toDto(order);
    }

    @Override
    @CacheEvict(value = {"orders", "order"}, key = "#orderId") 
    public void cancelOrder(Long orderId) {
    	 logger.info("Cancelling order with ID {}", orderId);
    	  Order order = orderRepository.findById(orderId)
    	            .orElseThrow(() -> {
    	                logger.error("Order with ID {} not found", orderId);
    	                return new EntityNotFoundException("Order", orderId);
    	            });

    	  if (order.getStatus() == OrderStatus.CANCELLED) {
    	        logger.error("Order with ID {} is already cancelled", orderId);
    	        throw new InvalidRequestException("Order with ID " + orderId + " is already cancelled.");
    	    }


        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        logger.info("Order with ID {} cancelled successfully", orderId);
    }

}
