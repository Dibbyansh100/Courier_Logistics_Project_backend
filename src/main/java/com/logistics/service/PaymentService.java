package com.logistics.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.Payment;
import com.logistics.entity.Shipment;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.PaymentRepository;
import com.logistics.repository.ShipmentRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service layer for Payment CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ShipmentRepository shipmentRepository;

//    // Create a new payment record
//    //  Create Payment
//    public ResponseStructure<Payment> createPayment(Payment payment) {
//        Payment saved = paymentRepository.save(payment);
//        ResponseStructure<Payment> res = new ResponseStructure<>();
//        res.setStatusCode(HttpStatus.CREATED.value());
//        res.setMessage("Payment created successfully");
//        res.setData(saved);
//        return res;
//    }

    // 1.Get all payments
    public ResponseStructure<List<Payment>> getAllPayments() {
        List<Payment> list = paymentRepository.findAll();
        ResponseStructure<List<Payment>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Payments retrieved");
        res.setData(list);
        return res;
    }

    // 2.Get all payments by its id
    public ResponseStructure<Payment> getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        ResponseStructure<Payment> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Payment retrieved");
        res.setData(payment);
        return res;
    }

    // 3. Get all payments done by a specific customer
    public ResponseStructure<List<Payment>> getAllPaymentsDoneByCustomer(Long customerId) {
        List<Shipment> shipments = shipmentRepository.findByCustomerId(customerId);
        List<Payment> payments = shipments.stream()
                .map(Shipment::getPayment)
                .filter(java.util.Objects::nonNull)
                .toList();
        ResponseStructure<List<Payment>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Payments for customer retrieved");
        res.setData(payments);
        return res;
    }

    // 4. Get payments by payment method
    public ResponseStructure<List<Payment>> getByPaymentMethod(String method) {
        List<Payment> list = paymentRepository.findByPaymentMethod(method);
        ResponseStructure<List<Payment>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Payments retrieved by method");
        res.setData(list);
        return res;
    }
    // 5.Update an existing payment
    public ResponseStructure<Payment> updatePayment(Long id, Payment updatedPayment) {
        Payment existing = getPaymentById(id).getData();
        existing.setAmount(updatedPayment.getAmount());
        existing.setPaymentMethod(updatedPayment.getPaymentMethod());
        existing.setPaymentStatus(updatedPayment.getPaymentStatus());
        // paymentDate and paymentTime are auto generated; ignore if passed
        Payment saved = paymentRepository.save(existing);
        ResponseStructure<Payment> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Payment updated");
        res.setData(saved);
        return res;
    }
}
