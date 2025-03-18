package com.ecommerce.dmart.mapper;

import com.ecommerce.dmart.dto.PaymentDTO;
import com.ecommerce.dmart.model.Payment;

public class PaymentMapper {
	
	public static PaymentDTO toDto(Payment payment) {
	 return new PaymentDTO(
             payment.getPaymentId(),
             payment.getOrder().getOrderId(),
             payment.getAmount(),
             payment.getPaymentMethod(),
             payment.getStatus(),
             payment.getPaymentDate()
     );
	}

}
