package com.logistics.controller;

import com.logistics.entity.Customer;
import com.logistics.service.CustomerService;
import com.logistics.dto.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

 // 1. Create Customer
    @PostMapping
    public ResponseEntity<ResponseStructure<Customer>> createCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.createCustomer(customer), HttpStatus.CREATED);
    }

    // 2. Get All Customers
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Customer>>> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }

    // 3. Get By Id
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Customer>> getById(@PathVariable Long id) {
        return new ResponseEntity<>(customerService.getById(id), HttpStatus.OK);
    }

    // 4. Get By Email
    @GetMapping("/email/{email}")
    public ResponseEntity<ResponseStructure<Customer>> getByEmail(@PathVariable String email) {
        return new ResponseEntity<>(customerService.getByEmail(email), HttpStatus.OK);
    }

    // 5. Get By Contact No
    @GetMapping("/contact/{contact}")
    public ResponseEntity<ResponseStructure<Customer>> getByContact(@PathVariable String contact) {
        return new ResponseEntity<>(customerService.getByContact(contact),HttpStatus.OK);
    }

    // 6. Get By Pagination AND Sorting
    @GetMapping("/page/{pageNumber}/{pageSize}/{sortBy}")
    public ResponseEntity<ResponseStructure<Page<Customer>>> getPaginated(@PathVariable int pageNumber, @PathVariable int pageSize,@PathVariable String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return new ResponseEntity<>(customerService.getCustomersWithPagination(pageable),HttpStatus.OK);
    }

    // 7. Update Customer
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<Customer>> updateCustomer(@PathVariable Long id,
                                                                     @RequestBody Customer customer) {
        return new ResponseEntity<>(customerService.updateCustomer(id, customer), HttpStatus.OK);
    }

    // 8. Delete Customer
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteCustomer(@PathVariable Long id) {
        return new ResponseEntity<>(customerService.deleteCustomer(id), HttpStatus.OK);
    }
}
