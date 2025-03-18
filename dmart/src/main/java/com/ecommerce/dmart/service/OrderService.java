package com.ecommerce.dmart.service;

import java.util.List;

import com.ecommerce.dmart.dto.OrderDTO;



public interface OrderService {
	
	 OrderDTO placeOrder(Long userId);
	 List<OrderDTO> getOrdersByUser(Long userId);
	 OrderDTO getOrderById(Long orderId);
	 void cancelOrder(Long orderId);

}
