package com.logistics.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.User;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Register / Create User
    public ResponseStructure<User> createUser(User user) {
        // Ensure role is formatted properly (e.g. ROLE_ADMIN, ROLE_CUSTOMER, ROLE_DELIVERY)
        if (user.getRole() != null && !user.getRole().startsWith("ROLE_")) {
            user.setRole("ROLE_" + user.getRole().toUpperCase());
        }
        User saved = userRepository.save(user);
        ResponseStructure<User> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("User registered successfully");
        res.setData(saved);
        return res;
    }

    // Get all users
    public ResponseStructure<List<User>> getAllUsers() {
        List<User> list = userRepository.findAll();
        ResponseStructure<List<User>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Users retrieved successfully");
        res.setData(list);
        return res;
    }

    // Get user by id
    public ResponseStructure<User> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        ResponseStructure<User> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("User retrieved successfully");
        res.setData(user);
        return res;
    }
}
