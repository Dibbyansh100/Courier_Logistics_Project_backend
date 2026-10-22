package com.logistics.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.TrackingHistory;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.TrackingHistoryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service layer for TrackingHistory CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class TrackingHistoryService {

    private final TrackingHistoryRepository trackingRepository;
    private final com.logistics.repository.ShipmentRepository shipmentRepository;

//    // Retrieve all tracking history entries
//    public ResponseStructure<List<TrackingHistory>> getAllTracking() {
//        List<TrackingHistory> list = trackingRepository.findAll();
//        ResponseStructure<List<TrackingHistory>> res = new ResponseStructure<>();
//        res.setStatusCode(HttpStatus.OK.value());
//        res.setMessage("Tracking histories retrieved");
//        res.setData(list);
//        return res;
//    }

    // 1.Get a tracking entry by its id
    public ResponseStructure<TrackingHistory> getTrackingById(Long id) {
        TrackingHistory tracking = trackingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TrackingHistory not found with id: " + id));
        ResponseStructure<TrackingHistory> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Tracking history retrieved");
        res.setData(tracking);
        return res;
    }

    // 2.Get all tracking entries for a specific shipment
    public ResponseStructure<List<TrackingHistory>> getByShipmentId(Long shipmentId) {
        List<TrackingHistory> list = trackingRepository.findByShipmentId(shipmentId);
        ResponseStructure<List<TrackingHistory>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Tracking histories for shipment retrieved");
        res.setData(list);
        return res;
    }

    // 3. Get tracking histories by shipment tracking number
    public ResponseStructure<List<TrackingHistory>> getByTrackingNumber(String trackingNumber) {
        var shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new com.logistics.exception.ResourceNotFoundException("Shipment not found with tracking number: " + trackingNumber));
        List<TrackingHistory> list = trackingRepository.findByShipmentId(shipment.getId());
        ResponseStructure<List<TrackingHistory>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Tracking histories for shipment retrieved");
        res.setData(list);
        return res;
    }

    // 4. Get tracking histories by status
    public ResponseStructure<List<TrackingHistory>> getByStatus(com.logistics.enums.ShipmentStatus status) {
        List<TrackingHistory> list = trackingRepository.findByStatus(status);
        ResponseStructure<List<TrackingHistory>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Tracking histories by status retrieved");
        res.setData(list);
        return res;
    }
}
