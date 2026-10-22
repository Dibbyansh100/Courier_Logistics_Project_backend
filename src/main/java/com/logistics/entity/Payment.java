package com.logistics.entity;

import com.logistics.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // BL-05: auto-calculated = weight * pricePerKg (+ fragile surcharge if applicable)
    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String paymentMethod;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    // Auto-set on creation via @CreationTimestamp
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate paymentDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime paymentTime;
}
