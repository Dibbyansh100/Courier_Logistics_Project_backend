package com.logistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.logistics.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payments by payment method (e.g., "Credit Card", "UPI")
    List<Payment> findByPaymentMethod(String paymentMethod);
}
