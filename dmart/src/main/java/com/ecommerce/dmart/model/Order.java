package com.ecommerce.dmart.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private double totalAmount;

	@OneToOne
	@JoinColumn(name = "cart_id", nullable = false, unique = true)
	private Cart cart;

	@Enumerated(EnumType.STRING)
	private OrderStatus status; // PENDING, COMPLETED, CANCELLED

	private LocalDateTime orderDate;

}
