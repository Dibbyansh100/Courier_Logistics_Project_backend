package com.logistics.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.logistics.entity.Shipment;
import com.logistics.enums.ShipmentStatus;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    List<Shipment> findByCustomerId(Long customerId);

    List<Shipment> findByWarehouseId(Long warehouseId);

    List<Shipment> findByDeliveryAgentId(Long deliveryAgentId);

    List<Shipment> findBySourceAndDestination(String source, String destination);

    List<Shipment> findByDeliveryDate(LocalDate deliveryDate);

    // BL-02: check if customer has any non-DELIVERED / active shipments
    boolean existsByCustomerIdAndStatusNot(Long customerId, ShipmentStatus status);

    // BL-03: check if agent is assigned to any shipment
    boolean existsByDeliveryAgentId(Long agentId);

    // BL-04: check if warehouse has any shipments linked
    boolean existsByWarehouseId(Long warehouseId);
}
