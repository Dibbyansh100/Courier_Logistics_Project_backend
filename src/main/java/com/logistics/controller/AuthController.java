package com.logistics.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.logistics.dto.LoginRequest;
import com.logistics.dto.LoginResponse;
import com.logistics.dto.ResponseStructure;
import com.logistics.entity.Customer;
import com.logistics.entity.DeliveryAgent;
import com.logistics.entity.User;
import com.logistics.repository.CustomerRepository;
import com.logistics.repository.DeliveryAgentRepository;
import com.logistics.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final CustomerRepository customerRepository;
    private final DeliveryAgentRepository agentRepository;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<LoginResponse>> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        // 1. Check Customer (by email or contact)
        Optional<Customer> customerOpt = customerRepository.findByEmail(username);
        if (customerOpt.isEmpty()) {
            customerOpt = customerRepository.findByContact(username);
        }
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (password != null && password.equals(customer.getPassword())) {
                LoginResponse resp = LoginResponse.builder()
                        .id(customer.getId())
                        .username(customer.getEmail())
                        .name(customer.getName())
                        .role(customer.getRole() != null ? customer.getRole() : "ROLE_CUSTOMER")
                        .tokenType("Basic")
                        .build();
                ResponseStructure<LoginResponse> res = new ResponseStructure<>();
                res.setStatusCode(HttpStatus.OK.value());
                res.setMessage("Customer login successful");
                res.setData(resp);
                return ResponseEntity.ok(res);
            }
        }

        // 2. Check DeliveryAgent (by contact)
        Optional<DeliveryAgent> agentOpt = agentRepository.findByContact(username);
        if (agentOpt.isPresent()) {
            DeliveryAgent agent = agentOpt.get();
            if (password != null && password.equals(agent.getPassword())) {
                LoginResponse resp = LoginResponse.builder()
                        .id(agent.getId())
                        .username(agent.getContact())
                        .name(agent.getName())
                        .role(agent.getRole() != null ? agent.getRole() : "ROLE_DELIVERY")
                        .tokenType("Basic")
                        .build();
                ResponseStructure<LoginResponse> structure = new ResponseStructure<>();
                structure.setStatusCode(HttpStatus.OK.value());
                structure.setMessage("Delivery Agent login successful");
                structure.setData(resp);
                return ResponseEntity.ok(structure);
            }
        }

        // 3. Check User (Admin)
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (password != null && password.equals(user.getPassword())) {
                LoginResponse resp = LoginResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getUsername())
                        .role(user.getRole() != null ? user.getRole() : "ROLE_ADMIN")
                        .tokenType("Basic")
                        .build();
                ResponseStructure<LoginResponse> structure = new ResponseStructure<>();
                structure.setStatusCode(HttpStatus.OK.value());
                structure.setMessage("Admin login successful");
                structure.setData(resp);
                return ResponseEntity.ok(structure);
            }
        }

        ResponseStructure<LoginResponse> error = new ResponseStructure<>();
        error.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        error.setMessage("Invalid username or password");
        error.setData(null);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
