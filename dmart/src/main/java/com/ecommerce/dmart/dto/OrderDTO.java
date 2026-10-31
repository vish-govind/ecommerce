package com.ecommerce.dmart.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
	
	private Long orderId;
    private Long userId;
    private String userName;  
    private String email;     
    private Long cartId;      
    private List<CartItemDTO> items;  
    private double totalAmount;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDate;


}
