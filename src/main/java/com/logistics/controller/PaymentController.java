package com.logistics.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.Payment;
import com.logistics.service.PaymentService;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for Payment CRUD operations.
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

//    // 1. Create Payment
//    @PostMapping
//    public ResponseEntity<ResponseStructure<Payment>> createPayment(@RequestBody Payment payment) {
//        return new ResponseEntity<>(paymentService.createPayment(payment), HttpStatus.CREATED);
//    }

 // 1. Get all Payments
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Payment>>> getAllPayments() {
        return new ResponseEntity<>(paymentService.getAllPayments(),HttpStatus.OK
        );
    }

    // 2. Get Payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Payment>> getPaymentById(
            @PathVariable Long id) {
        return new ResponseEntity<>(paymentService.getPaymentById(id),HttpStatus.OK
        );
    }

    // 3. Get all payments for a customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ResponseStructure<List<Payment>>> getAllPaymentsDoneByCustomer(
            @PathVariable Long customerId) {
        return new ResponseEntity<>(paymentService.getAllPaymentsDoneByCustomer(customerId), HttpStatus.OK
        );
    }

    // 4. Get payments by method
    @GetMapping("/method/{method}")
    public ResponseEntity<ResponseStructure<List<Payment>>> getByPaymentMethod(
            @PathVariable String method) {
        return new ResponseEntity<>(paymentService.getByPaymentMethod(method),HttpStatus.OK
        );
    }

    // 5. Update Payment
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<Payment>> updatePayment(
            @PathVariable Long id,
            @RequestBody Payment payment) {
        return new ResponseEntity<>(paymentService.updatePayment(id, payment),HttpStatus.OK
        );
    }
}
