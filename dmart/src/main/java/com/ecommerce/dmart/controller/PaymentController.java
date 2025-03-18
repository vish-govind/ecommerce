package com.ecommerce.dmart.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dmart.dto.PaymentDTO;
import com.ecommerce.dmart.service.PaymentService;

@RequestMapping
@RestController("/payments")
public class PaymentController {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PaymentDTO> processPayment(@PathVariable Long orderId, @RequestParam String paymentMethod) {
    	 logger.info("Processing payment for order {} using {}", orderId, paymentMethod);
         PaymentDTO payment = paymentService.processPayment(orderId, paymentMethod);
         logger.info("Payment processed successfully for order {}", orderId);
         return ResponseEntity.ok(payment);
    }

}
