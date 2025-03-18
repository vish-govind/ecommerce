package com.ecommerce.dmart.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.ecommerce.dmart.dto.PaymentDTO;
import com.ecommerce.dmart.exception.EntityNotFoundException;
import com.ecommerce.dmart.exception.InvalidRequestException;
import com.ecommerce.dmart.mapper.PaymentMapper;
import com.ecommerce.dmart.model.Cart;
import com.ecommerce.dmart.model.Order;
import com.ecommerce.dmart.model.OrderStatus;
import com.ecommerce.dmart.model.Payment;
import com.ecommerce.dmart.repository.CartRepository;
import com.ecommerce.dmart.repository.OrderRepository;
import com.ecommerce.dmart.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;

	public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository,
			CartRepository cartRepository) {
		super();
		this.paymentRepository = paymentRepository;
		this.orderRepository = orderRepository;
		this.cartRepository = cartRepository;
	}

	@Override
	@CachePut(value = "payments", key = "#orderId") // Cache the payment for an order
	@CacheEvict(value = "order", key = "#orderId") // Remove cached order to ensure updated data
	public PaymentDTO processPayment(Long orderId, String paymentMethod) {

		logger.info("Processing payment for order ID {} with method {}", orderId, paymentMethod);

		Order order = orderRepository.findById(orderId).orElseThrow(() -> {
			logger.error("Order with ID {} not found", orderId);
			return new EntityNotFoundException("Order", orderId);
		});

		if (order.getTotalAmount() <= 0) {
			logger.warn("Payment cannot be processed for order ID {} with zero or negative amount", orderId);
			throw new InvalidRequestException("Payment cannot be processed for an order with zero or negative amount.");
		}

		// Process Payment
		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setAmount(order.getTotalAmount());
		payment.setPaymentMethod(paymentMethod);
		payment.setStatus("COMPLETED");
		payment.setPaymentDate(LocalDateTime.now());

		Payment savedPayment = paymentRepository.save(payment);
		logger.info("Payment processed successfully for order ID {}", orderId);

		// After successful payment, clear cart and update order status
		Cart cart = order.getCart();
		cart.getItems().clear();
		cart.setTotalAmount(0);
		cartRepository.save(cart);

		order.setStatus(OrderStatus.COMPLETED);
		orderRepository.save(order); // Update order status after payment
		logger.info("Order ID {} status updated to COMPLETED and cart cleared", orderId);
		return PaymentMapper.toDto(savedPayment);
	}
}
