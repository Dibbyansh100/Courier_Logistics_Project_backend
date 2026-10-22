package com.logistics.repository;

import com.logistics.entity.DeliveryAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {

    Optional<DeliveryAgent> findByVehicleNo(String vehicleNo);

    Optional<DeliveryAgent> findByContact(String contact);

    // BL: get agents with rating greater than a given value (double format)
    List<DeliveryAgent> findByRatingGreaterThan(Double rating);

    // Sort agents by rating descending
    List<DeliveryAgent> findAllByOrderByRatingDesc();
    
 // Validation helper – contact must be exactly 10 digits (BL‑01)
    default void validateContact(String contact) {
       if (contact == null || !contact.matches("\\d{10}")) {
           throw new IllegalArgumentException("Contact number must be exactly 10 digits.");
       }
   }
}
