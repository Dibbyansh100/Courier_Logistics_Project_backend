package com.logistics.controller;

import com.logistics.entity.DeliveryAgent;
import com.logistics.service.DeliveryAgentService;
import com.logistics.dto.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class DeliveryAgentController {

    private final DeliveryAgentService agentService;

 // 1. Create Agent
    @PostMapping
    public ResponseEntity<ResponseStructure<DeliveryAgent>> createAgent(@RequestBody DeliveryAgent agent) {
        return new ResponseEntity<>(agentService.createAgent(agent), HttpStatus.CREATED);
    }
    // 2. Get All Agents
    @GetMapping
    public ResponseEntity<ResponseStructure<List<DeliveryAgent>>> getAllAgents() {
        return new ResponseEntity<>(agentService.getAllAgents(),HttpStatus.OK);
    }
    // 3. Get By Id
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<DeliveryAgent>> getById(@PathVariable Long id) {
        return new ResponseEntity<>(agentService.getById(id),HttpStatus.OK);
    }
    // 4. Get By Vehicle No
    @GetMapping("/vehicle/{vehicleNo}")
    public ResponseEntity<ResponseStructure<DeliveryAgent>> getByVehicleNo(@PathVariable String vehicleNo) {
        return new ResponseEntity<>(agentService.getByVehicleNo(vehicleNo), HttpStatus.OK);
    }
    // 5. Get By Contact
    @GetMapping("/contact/{contact}")
    public ResponseEntity<ResponseStructure<DeliveryAgent>> getByContact(@PathVariable String contact) {
        return new ResponseEntity<>(agentService.getByContact(contact), HttpStatus.OK);
    }
    // 6. Get By Rating Greater Than (double)
    @GetMapping("/rating")
    public ResponseEntity<ResponseStructure<List<DeliveryAgent>>> getByRatingGreaterThan(
            @RequestParam Double minRating) {
        return new ResponseEntity<>(agentService.getByRatingGreaterThan(minRating), HttpStatus.OK);
    }
    // 7. Update Delivery Agent Details
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<DeliveryAgent>> updateAgent(@PathVariable Long id,
                                                                       @RequestBody DeliveryAgent agent) {
        return new ResponseEntity<>(agentService.updateAgent(id, agent), HttpStatus.OK);
    }
    // 8. Update Status (placeholder – status handled in Shipment)
    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseStructure<DeliveryAgent>> updateStatus(@PathVariable Long id,
                                                                        @RequestParam String status) {
        // The service currently ignores the status parameter; you can map it to ShipmentStatus if needed.
        return new ResponseEntity<>(agentService.updateStatus(id, null), HttpStatus.OK);
    }
    // 9. Update Availability
    @PutMapping("/{id}/availability")
    public ResponseEntity<ResponseStructure<DeliveryAgent>> updateAvailability(@PathVariable Long id,
                                                                              @RequestParam Boolean availability) {
        return new ResponseEntity<>(agentService.updateAvailability(id, availability), HttpStatus.OK);
    }
    // 10. Sort By Rating (descending)
    @GetMapping("/sorted/rating")
    public ResponseEntity<ResponseStructure<List<DeliveryAgent>>> sortByRating() {
        return new ResponseEntity<>(agentService.sortByRating(), HttpStatus.OK);
    }
    // 11. Delete Agent 
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteAgent(@PathVariable Long id) {
        return new ResponseEntity<>(agentService.deleteAgent(id), HttpStatus.OK);
    }
}
