package com.ecommerce.dmart.service;

import com.ecommerce.dmart.dto.PaymentDTO;

public interface PaymentService {
	
	PaymentDTO processPayment(Long orderId, String paymentMethod);

}
