package com.ecommerce.dmart.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
	
	private Long paymentId;
	private Long orderId;
	private double amount;
	private String paymentMethod;
	private String status;
	private LocalDateTime paymentDate;

}
