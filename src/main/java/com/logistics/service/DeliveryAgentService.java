package com.logistics.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.DeliveryAgent;
import com.logistics.enums.ShipmentStatus;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.DeliveryAgentRepository;
import com.logistics.repository.ShipmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryAgentService {

    private final DeliveryAgentRepository agentRepository;
    private final ShipmentRepository shipmentRepository;

    // 1. Create Agent
    public ResponseStructure<DeliveryAgent> createAgent(DeliveryAgent agent) {
    	agentRepository.validateContact(agent.getContact());
        if (agent.getRole() == null || agent.getRole().isBlank()) {
            agent.setRole("ROLE_DELIVERY");
        }
        DeliveryAgent saved = agentRepository.save(agent);
        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Delivery agent created");
        res.setData(saved);
        return res;
    }
     
    // 2. Get All Agents
    public ResponseStructure<List<DeliveryAgent>> getAllAgents() {
        List<DeliveryAgent> list = agentRepository.findAll();
        ResponseStructure<List<DeliveryAgent>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delivery agents retrieved");
        res.setData(list);
        return res;
    }
     
    // 3. Get By Id
    public ResponseStructure<DeliveryAgent> getById(Long id) {
        DeliveryAgent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Agent not found with id: " + id));
        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delivery agent retrieved");
        res.setData(agent);
        return res;
    }
     
    // 4. Get By Vehicle No
    public ResponseStructure<DeliveryAgent> getByVehicleNo(String vehicleNo) {
        DeliveryAgent agent = agentRepository.findByVehicleNo(vehicleNo)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Agent not found with vehicleNo: " + vehicleNo));
        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delivery agent retrieved by vehicle number");
        res.setData(agent);
        return res;
    }
     
    // 5. Get By Contact
     public ResponseStructure<DeliveryAgent> getByContact(String contact) {
        DeliveryAgent agent = agentRepository.findByContact(contact)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Agent not found with contact: " + contact));
        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delivery agent retrieved by contact");
        res.setData(agent);
        return res;
    }
     
    // 6. Get By Rating Greater Than (double value)
     public ResponseStructure<List<DeliveryAgent>> getByRatingGreaterThan(Double rating) {
        List<DeliveryAgent> list = agentRepository.findByRatingGreaterThan(rating);
        ResponseStructure<List<DeliveryAgent>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delivery agents with rating > " + rating);
        res.setData(list);
        return res;
    }
     // 7. Update Delivery Agent Details    
     public ResponseStructure<DeliveryAgent> updateAgent(Long id, DeliveryAgent updatedAgent) {
        DeliveryAgent existing = getById(id).getData();
        agentRepository.validateContact(updatedAgent.getContact());
        existing.setName(updatedAgent.getName());
        existing.setContact(updatedAgent.getContact());
        existing.setVehicleNo(updatedAgent.getVehicleNo());
        existing.setRating(updatedAgent.getRating());
        if (updatedAgent.getPassword() != null) {
            existing.setPassword(updatedAgent.getPassword());
        }
        DeliveryAgent saved = agentRepository.save(existing);
        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delivery agent updated");
        res.setData(saved);
        return res;
    }
     
    // 8. Update Status (status flag is handled in Shipment, but we keep the API)
    public ResponseStructure<DeliveryAgent> updateStatus(Long id, ShipmentStatus status) {
        // Currently the status is managed on Shipment; we simply return the agent
        DeliveryAgent agent = getById(id).getData();
        DeliveryAgent saved = agentRepository.save(agent);
        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delivery agent status updated");
        res.setData(saved);
        return res;
    }
     
    // 9. Update Availability — BL‑06 (used by shipment guard)
    public ResponseStructure<DeliveryAgent> updateAvailability(Long id, Boolean availability) {
        DeliveryAgent agent = getById(id).getData();
        agent.setAvailability(availability);
        DeliveryAgent saved = agentRepository.save(agent);
        ResponseStructure<DeliveryAgent> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delivery agent availability updated");
        res.setData(saved);
        return res;
    }
     
    // 10. Sort By Rating (descending)
      public ResponseStructure<List<DeliveryAgent>> sortByRating() {
        List<DeliveryAgent> list = agentRepository.findAllByOrderByRatingDesc();
        ResponseStructure<List<DeliveryAgent>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delivery agents sorted by rating");
        res.setData(list);
        return res;
    }
     
    // 11. Delete Agent — BL‑03 (prevent if assigned to any shipment)
     public ResponseStructure<String> deleteAgent(Long id) {
        DeliveryAgent agent = getById(id).getData();
        boolean isAssigned = shipmentRepository.existsByDeliveryAgentId(id);
        if (isAssigned) {
            throw new IllegalStateException("Cannot delete Delivery Agent [id=" + id + "]: they are currently assigned to a shipment.");
        }
        agentRepository.delete(agent);
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Delivery agent deleted");
        res.setData("Delivery Agent deleted successfully");
        return res;
    }
}
