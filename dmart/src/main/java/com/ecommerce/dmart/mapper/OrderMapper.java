package com.ecommerce.dmart.mapper;

import java.util.stream.Collectors;

import com.ecommerce.dmart.dto.OrderDTO;
import com.ecommerce.dmart.model.Order;

public class OrderMapper {
	
	  // Convert Order entity to OrderDTO
    public static OrderDTO toDto(Order order) {
        if (order == null) {
            return null;
        }

        return new OrderDTO(
                order.getOrderId(),
                order.getUser().getUserId(),
                order.getUser().getUserName(), 
                order.getUser().getEmail(),     
                order.getCart().getCartId(),
                order.getCart().getItems().stream()
                .map(cartItem -> CartItemMapper.toDTO(cartItem)) 
                .collect(Collectors.toList()),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getOrderDate()
        );
    }

}
