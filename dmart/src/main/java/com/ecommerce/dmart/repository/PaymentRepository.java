package com.ecommerce.dmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.dmart.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
