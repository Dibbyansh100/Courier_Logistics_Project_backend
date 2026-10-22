package com.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_agents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String password;

    @Builder.Default
    @Column(nullable = true)
    private String role = "ROLE_DELIVERY";

    // Unique and exactly 10 digits (enforced in service layer)
    @Column(nullable = false, unique = true, length = 10)
    private String contact;

    @Column(nullable = false, unique = true)
    private String vehicleNo;

    // BL-06: used to prevent assignment if false
    @Builder.Default
    @Column(nullable = false)
    private Boolean availability = true;

    // BL: rating in double format for filtering
    @Builder.Default
    @Column(nullable = false)
    private Double rating = 0.0;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
