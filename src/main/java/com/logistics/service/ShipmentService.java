package com.logistics.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.Customer;
import com.logistics.entity.DeliveryAgent;
import com.logistics.entity.PackageEntity;
import com.logistics.entity.Payment;
import com.logistics.entity.Shipment;
import com.logistics.entity.Warehouse;
import com.logistics.enums.ShipmentStatus;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.CustomerRepository;
import com.logistics.repository.DeliveryAgentRepository;
import com.logistics.repository.ShipmentRepository;
import com.logistics.repository.WarehouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository    shipmentRepository;
    private final CustomerRepository    customerRepository;
    private final DeliveryAgentRepository agentRepository;
    private final WarehouseRepository   warehouseRepository;

    // Pricing constants (BL-05)
    private static final double PRICE_PER_KG      = 50.0;  // ₹50 per kg
    private static final double FRAGILE_SURCHARGE  = 100.0; // ₹100 flat for fragile packages

 // 1. Create Shipment — BL‑05: auto‑calculate payment amount
    public ResponseStructure<Shipment> createShipment(Shipment shipment) {
        // Validate customer exists
        Customer customer = customerRepository.findById(shipment.getCustomer().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + shipment.getCustomer().getId()));
        shipment.setCustomer(customer);
        // Auto‑generate unique tracking number
        shipment.setTrackingNumber("TRK-" + UUID.randomUUID().toString()
                .substring(0, 8).toUpperCase());
        // 5: calculate payment amount 
        PackageEntity pkg = shipment.getPackageEntity();
        double amount = shipment.getWeight() * PRICE_PER_KG;
        if (pkg != null && Boolean.TRUE.equals(pkg.getFragile())) {
            amount += FRAGILE_SURCHARGE;
        }
        // Set calculated amount on payment
        Payment payment = shipment.getPayment();
        if (payment == null) {
            payment = new Payment();
        }
        payment.setAmount(amount);
        shipment.setPayment(payment);
        shipment.setStatus(ShipmentStatus.PENDING);
        Shipment saved = shipmentRepository.save(shipment);
        ResponseStructure<Shipment> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Shipment created");
        res.setData(saved);
        return res;
    }
     
    // 2. Get All Shipments
    public ResponseStructure<List<Shipment>> getAllShipments() {
        List<Shipment> list = shipmentRepository.findAll();
        ResponseStructure<List<Shipment>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Shipments retrieved");
        res.setData(list);
        return res;
    }
     
    // 3. Get By Id
    public ResponseStructure<Shipment> getById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        ResponseStructure<Shipment> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Shipment retrieved");
        res.setData(shipment);
        return res;
    }
     
    // 4. Get By Tracking Number
    public ResponseStructure<Shipment> getByTrackingNumber(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipment not found with trackingNumber: " + trackingNumber));
        ResponseStructure<Shipment> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Shipment retrieved by tracking number");
        res.setData(shipment);
        return res;
    }
     
    // 5. Get By Customer
    public ResponseStructure<List<Shipment>> getByCustomer(Long customerId) {
        List<Shipment> list = shipmentRepository.findByCustomerId(customerId);
        ResponseStructure<List<Shipment>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Shipments by customer retrieved");
        res.setData(list);
        return res;
    }
     
    // 6. Get By Warehouse
    public ResponseStructure<List<Shipment>> getByWarehouse(Long warehouseId) {
        List<Shipment> list = shipmentRepository.findByWarehouseId(warehouseId);
        ResponseStructure<List<Shipment>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Shipments by warehouse retrieved");
        res.setData(list);
        return res;
    }
     
    // 7. Get By Delivery Agent
    public ResponseStructure<List<Shipment>> getByDeliveryAgent(Long agentId) {
        List<Shipment> list = shipmentRepository.findByDeliveryAgentId(agentId);
        ResponseStructure<List<Shipment>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Shipments by delivery agent retrieved");
        res.setData(list);
        return res;
    }
     
    // 8. Get By Source And Destination
    public ResponseStructure<List<Shipment>> getBySourceAndDestination(String source, String destination) {
        List<Shipment> list = shipmentRepository.findBySourceAndDestination(source, destination);
        ResponseStructure<List<Shipment>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Shipments by source and destination retrieved");
        res.setData(list);
        return res;
    }
     
    // 9. Get By Delivery Date
    public ResponseStructure<List<Shipment>> getByDeliveryDate(LocalDate deliveryDate) {
        List<Shipment> list = shipmentRepository.findByDeliveryDate(deliveryDate);
        ResponseStructure<List<Shipment>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Shipments by delivery date retrieved");
        res.setData(list);
        return res;
    }
     
    // 10. Get By Pagination And Sorting
    public ResponseStructure<Page<Shipment>> getShipmentsWithPagination(Pageable pageable) {
        Page<Shipment> page = shipmentRepository.findAll(pageable);
        ResponseStructure<Page<Shipment>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Shipments page retrieved");
        res.setData(page);
        return res;
    }
    
    // 11. Assign Delivery Agent — BL‑06 (block if agent unavailable)
    public ResponseStructure<Shipment> assignDeliveryAgent(Long shipmentId, Long agentId) {
        // Extract the actual Shipment entity from the wrapper response
        Shipment shipment = getById(shipmentId).getData();
        DeliveryAgent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Delivery Agent not found with id: " + agentId));
        // 6: reject if agent is not available
        if (Boolean.FALSE.equals(agent.getAvailability())) {
            throw new IllegalStateException(
                    "Cannot assign Delivery Agent [id=" + agentId + "]: agent is currently unavailable.");
        }
        // Assign and update status
        shipment.setDeliveryAgent(agent);
        shipment.setStatus(ShipmentStatus.IN_TRANSIT);
        // Mark agent as unavailable after assignment
        agent.setAvailability(false);
        agentRepository.save(agent);
        Shipment saved = shipmentRepository.save(shipment);
        ResponseStructure<Shipment> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delivery agent assigned to shipment");
        res.setData(saved);
        return res;
    }
     
    // 12. Assign Warehouse
    public ResponseStructure<Shipment> assignWarehouse(Long shipmentId, Long warehouseId) {
        Shipment shipment = getById(shipmentId).getData();
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found with id: " + warehouseId));
        shipment.setWarehouse(warehouse);
        // Optionally update status if needed
        Shipment saved = shipmentRepository.save(shipment);
        ResponseStructure<Shipment> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Warehouse assigned to shipment");
        res.setData(saved);
        return res;
    }
    
     // 13. Delete Shipment
     public ResponseStructure<String> deleteShipment(Long id) {
        Shipment shipment = getById(id).getData();
        shipmentRepository.delete(shipment);
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Shipment deleted");
        res.setData("Shipment deleted successfully");
        return res;
    }
}
