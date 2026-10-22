package com.logistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.logistics.entity.TrackingHistory;
import com.logistics.enums.ShipmentStatus;

@Repository
public interface TrackingHistoryRepository extends JpaRepository<TrackingHistory, Long> {

    List<TrackingHistory> findByShipmentId(Long shipmentId);

    // Find tracking entries by status
    List<TrackingHistory> findByStatus(ShipmentStatus status);
}
