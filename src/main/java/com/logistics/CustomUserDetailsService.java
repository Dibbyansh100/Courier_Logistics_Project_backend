package com.logistics;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.logistics.entity.Customer;
import com.logistics.entity.DeliveryAgent;
import com.logistics.entity.User;
import com.logistics.repository.CustomerRepository;
import com.logistics.repository.DeliveryAgentRepository;
import com.logistics.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DeliveryAgentRepository agentRepository;

    @Autowired(required = false)
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Check if Customer exists (login by email)
        Optional<Customer> customerOpt = customerRepository.findByEmail(username);
        if (customerOpt.isPresent()) {
            return new CustomUserDetails(customerOpt.get());
        }

        // Check if Customer exists (login by contact)
        Optional<Customer> customerByContact = customerRepository.findByContact(username);
        if (customerByContact.isPresent()) {
            return new CustomUserDetails(customerByContact.get());
        }

        // 2. Check if DeliveryAgent exists (login by contact number)
        Optional<DeliveryAgent> agentOpt = agentRepository.findByContact(username);
        if (agentOpt.isPresent()) {
            return new CustomUserDetails(agentOpt.get());
        }

        // 3. Check if Admin/User exists
        if (userRepository != null) {
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                return new CustomUserDetails(userOpt.get());
            }
        }

        throw new UsernameNotFoundException("User not available with identifier: " + username);
    }
}
