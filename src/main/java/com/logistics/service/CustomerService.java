package com.logistics.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.Customer;
import com.logistics.enums.ShipmentStatus;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.CustomerRepository;
import com.logistics.repository.ShipmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ShipmentRepository shipmentRepository;

 // 1. Create Customer
    public ResponseStructure<Customer> createCustomer(Customer customer) {
        validateContact(customer.getContact());
        if (customer.getRole() == null || customer.getRole().isBlank()) {
            customer.setRole("ROLE_CUSTOMER");
        }
        Customer saved = customerRepository.save(customer);
        ResponseStructure<Customer> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Customer created successfully");
        res.setData(saved);
        return res;
    }

    // 2. Get All Customers
    public ResponseStructure<List<Customer>> getAllCustomers() {
        List<Customer> list = customerRepository.findAll();
        ResponseStructure<List<Customer>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Customers retrieved");
        res.setData(list);
        return res;
    }

    // 3. Get By Id
    public ResponseStructure<Customer> getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        ResponseStructure<Customer> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Customer retrieved");
        res.setData(customer);
        return res;
    }

    // 4. Get By Email
    public ResponseStructure<Customer> getByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
        ResponseStructure<Customer> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Customer retrieved by email");
        res.setData(customer);
        return res;
    }

    // 5. Get By Contact No
    public ResponseStructure<Customer> getByContact(String contact) {
        Customer customer = customerRepository.findByContact(contact)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with contact: " + contact));
        ResponseStructure<Customer> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Customer retrieved by contact");
        res.setData(customer);
        return res;
    }

    // 6. Get By Pagination And Sorting
    public ResponseStructure<Page<Customer>> getCustomersWithPagination(Pageable pageable) {
        Page<Customer> page = customerRepository.findAll(pageable);
        ResponseStructure<Page<Customer>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Customers page retrieved");
        res.setData(page);
        return res;
    }

    // 7. Update Customer
    public ResponseStructure<Customer> updateCustomer(Long id, Customer updatedCustomer) {
        Customer existing = getById(id).getData();
        validateContact(updatedCustomer.getContact());
        existing.setName(updatedCustomer.getName());
        existing.setEmail(updatedCustomer.getEmail());
        existing.setContact(updatedCustomer.getContact());
        existing.setAddress(updatedCustomer.getAddress());
        if (updatedCustomer.getPassword() != null) {
            existing.setPassword(updatedCustomer.getPassword());
        }
        Customer saved = customerRepository.save(existing);
        ResponseStructure<Customer> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Customer updated");
        res.setData(saved);
        return res;
    }

    // 8. Delete Customer — BL‑02: block if pending/undelivered shipments exist
    public ResponseStructure<String> deleteCustomer(Long id) {
        Customer customer = getById(id).getData();
        boolean hasPendingShipments = shipmentRepository
                .existsByCustomerIdAndStatusNot(id, ShipmentStatus.DELIVERED);
        if (hasPendingShipments) {
            throw new IllegalStateException(
                    "Cannot delete Customer [id=" + id + "]: they have pending or undelivered shipments.");
        }
        customerRepository.delete(customer);
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Customer deleted");
        res.setData("Customer deleted successfully");
        return res;
    }
    // Validate contact number (10 digits)
    private void validateContact(String contact) {
        if (contact == null || !contact.matches("\\d{10}")) {
            throw new IllegalArgumentException("Contact number must be exactly 10 digits.");
        }
    }
}
