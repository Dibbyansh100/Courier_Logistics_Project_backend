package com.logistics.repository;

import com.logistics.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    List<Warehouse> findByLocation(String location);

    Optional<Warehouse> findByContact(String contact);
    
    // Validation helper – contact must be exactly 10 digits (BL‑01)
    // -----------------------------------------------------------------
    default void validateContact(String contact) {
        if (contact == null || !contact.matches("\\d{10}")) {
            throw new IllegalArgumentException(
                    "Contact number must be exactly 10 digits."
            );
        }
    }
}
