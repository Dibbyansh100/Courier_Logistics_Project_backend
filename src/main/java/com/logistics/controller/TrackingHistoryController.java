package com.logistics.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.TrackingHistory;
import com.logistics.service.TrackingHistoryService;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for TrackingHistory CRUD operations.
 */
@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class TrackingHistoryController {

    private final TrackingHistoryService trackingService;

//    // 2. Get all tracking histories
//    @GetMapping
//    public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getAllTracking() {
//        return ResponseEntity.ok(trackingService.getAllTracking());
//    }

 // 1. Get tracking history by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<TrackingHistory>> getTrackingById(
            @PathVariable Long id) {
       return new ResponseEntity<>(trackingService.getTrackingById(id),HttpStatus.OK);
    }

    // 2. Get all tracking entries for a specific shipment
    @GetMapping("/shipment/{shipmentId}")
    public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getByShipmentId(
            @PathVariable Long shipmentId) {
       return new ResponseEntity<>(trackingService.getByShipmentId(shipmentId),HttpStatus.OK);
    }

    // 3. Get tracking histories by shipment tracking number
    @GetMapping("/shipment/tracking/{trackingNumber}")
    public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getByTrackingNumber(
            @PathVariable String trackingNumber) {
       return new ResponseEntity<>( trackingService.getByTrackingNumber(trackingNumber),HttpStatus.OK);
    }

    // 4. Get tracking histories by status
    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseStructure<List<TrackingHistory>>> getByStatus(
            @PathVariable com.logistics.enums.ShipmentStatus status) {
         return new ResponseEntity<>(trackingService.getByStatus(status), HttpStatus.OK);
    }
}
