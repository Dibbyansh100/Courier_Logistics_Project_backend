package com.logistics.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.Shipment;
import com.logistics.service.ShipmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;


    // 1. Create Shipment — auto-calculates payment amount (BL-05)
       @PostMapping
       public ResponseEntity<ResponseStructure<Shipment>> createShipment(@RequestBody Shipment shipment) {
           return new ResponseEntity<>(shipmentService.createShipment(shipment), HttpStatus.CREATED);
       }

       // 2. Get All Shipments
       @GetMapping
       public ResponseEntity<ResponseStructure<List<Shipment>>> getAllShipments() {
           return new ResponseEntity<>(shipmentService.getAllShipments(), HttpStatus.OK);
       }

       // 3. Get By Id
       @GetMapping("/{id}")
       public ResponseEntity<ResponseStructure<Shipment>> getById(@PathVariable Long id) {
           return new ResponseEntity<>(shipmentService.getById(id), HttpStatus.OK);
       }

       // 4. Get By Tracking Number
       @GetMapping("/tracking/{trackingNumber}")
       public ResponseEntity<ResponseStructure<Shipment>> getByTrackingNumber(@PathVariable String trackingNumber) {
           return new ResponseEntity<>(shipmentService.getByTrackingNumber(trackingNumber), HttpStatus.OK);
       }

       // 5. Get By Customer
       @GetMapping("/customer/{customerId}")
       public ResponseEntity<ResponseStructure<List<Shipment>>> getByCustomer(@PathVariable Long customerId) {
           return new ResponseEntity<>(shipmentService.getByCustomer(customerId), HttpStatus.OK);
       }

       // 6. Get By Warehouse
       @GetMapping("/warehouse/{warehouseId}")
       public ResponseEntity<ResponseStructure<List<Shipment>>> getByWarehouse(@PathVariable Long warehouseId) {
           return new ResponseEntity<>(shipmentService.getByWarehouse(warehouseId), HttpStatus.OK);
       }

       // 7. Get By Delivery Agent
       @GetMapping("/agent/{agentId}")
       public ResponseEntity<ResponseStructure<List<Shipment>>> getByDeliveryAgent(@PathVariable Long agentId) {
           return new ResponseEntity<>(shipmentService.getByDeliveryAgent(agentId), HttpStatus.OK);
       }

       // 8. Get By Source And Destination
       @GetMapping("/route")
       public ResponseEntity<ResponseStructure<List<Shipment>>> getBySourceAndDestination(
               @RequestParam String source,
               @RequestParam String destination) {
           return new ResponseEntity<>(shipmentService.getBySourceAndDestination(source, destination),HttpStatus.OK);
       }

       // 9. Get By Delivery Date
       @GetMapping("/delivery-date/{date}")
       public ResponseEntity<ResponseStructure<List<Shipment>>> getByDeliveryDate(
               @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
           return new ResponseEntity<>(shipmentService.getByDeliveryDate(date), HttpStatus.OK);
       }

       // 10. Get By Pagination And Sorting
       @GetMapping("/paginated")
       public ResponseEntity<ResponseStructure<Page<Shipment>>> getPaginated(
               @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "10") int size,
               @RequestParam(defaultValue = "id") String sort) {

           Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

           return new ResponseEntity<>(shipmentService.getShipmentsWithPagination(pageable),HttpStatus.OK);
       }

       // 11. Assign Delivery Agent — BL-06 (blocks if unavailable)
       @PutMapping("/{shipmentId}/assign-agent/{agentId}")
       public ResponseEntity<ResponseStructure<Shipment>> assignDeliveryAgent(
               @PathVariable Long shipmentId,
               @PathVariable Long agentId) {
           return new ResponseEntity<>(shipmentService.assignDeliveryAgent(shipmentId, agentId),HttpStatus.OK);
       }

       // 12. Assign Warehouse
       @PutMapping("/{shipmentId}/assign-warehouse/{warehouseId}")
       public ResponseEntity<ResponseStructure<Shipment>> assignWarehouse(
               @PathVariable Long shipmentId,
               @PathVariable Long warehouseId) {
           return new ResponseEntity<>(shipmentService.assignWarehouse(shipmentId, warehouseId),HttpStatus.OK);
       }

       // 13. Delete Shipment
       @DeleteMapping("/{id}")
       public ResponseEntity<ResponseStructure<String>> deleteShipment(@PathVariable Long id) {
           return new ResponseEntity<>(shipmentService.deleteShipment(id), HttpStatus.OK);
       }

}
