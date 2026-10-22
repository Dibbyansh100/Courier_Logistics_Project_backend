package com.logistics.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.Warehouse;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.ShipmentRepository;
import com.logistics.repository.WarehouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ShipmentRepository shipmentRepository;

 // 1. Create Warehouse
 // -----------------------------------------------------------------
 public ResponseStructure<Warehouse> createWarehouse(Warehouse warehouse) {
     warehouseRepository.validateContact(warehouse.getContact());

     Warehouse saved = warehouseRepository.save(warehouse);
     ResponseStructure<Warehouse> res = new ResponseStructure<>();
     res.setStatusCode(HttpStatus.CREATED.value());
     res.setMessage("Warehouse created");
     res.setData(saved);
     return res;
 }
    
    // 2. Get All Warehouses
    public ResponseStructure<List<Warehouse>> getAllWarehouses() {
        List<Warehouse> list = warehouseRepository.findAll();
        ResponseStructure<List<Warehouse>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Warehouses retrieved");
        res.setData(list);
        return res;
    }
     
    // 3. Get By Id
         public ResponseStructure<Warehouse> getById(Long id) {
        Warehouse ware = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
        ResponseStructure<Warehouse> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Warehouse retrieved");
        res.setData(ware);
        return res;
    }
         
    // 4. Get By Location
    public ResponseStructure<List<Warehouse>> getByLocation(String location) {
        List<Warehouse> list = warehouseRepository.findByLocation(location);
        ResponseStructure<List<Warehouse>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Warehouses by location retrieved");
        res.setData(list);
        return res;
    }
    
    // 5. Get By Contact No
    public ResponseStructure<Warehouse> getByContact(String contact) {
        Warehouse ware = warehouseRepository.findByContact(contact)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with contact: " + contact));
        ResponseStructure<Warehouse> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Warehouse retrieved by contact");
        res.setData(ware);
        return res;
    }
    
    // 6. Update Warehouse
    public ResponseStructure<Warehouse> updateWarehouse(Long id, Warehouse updatedWarehouse) {
        Warehouse existing = getById(id).getData();
        warehouseRepository.validateContact(updatedWarehouse.getContact());
        existing.setName(updatedWarehouse.getName());
        existing.setLocation(updatedWarehouse.getLocation());
        existing.setCapacity(updatedWarehouse.getCapacity());
        existing.setContact(updatedWarehouse.getContact());
        Warehouse saved = warehouseRepository.save(existing);
        ResponseStructure<Warehouse> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Warehouse updated");
        res.setData(saved);
        return res;
    }
    
    // 7. Delete Warehouse — BL‑04 (block if any shipment is linked)
    public ResponseStructure<String> deleteWarehouse(Long id) {
        Warehouse warehouse = getById(id).getData();
        boolean hasShipments = shipmentRepository.existsByWarehouseId(id);
        if (hasShipments) {
            throw new IllegalStateException(
                    "Cannot delete Warehouse [id=" + id + "]: shipments exist at this warehouse.");
        }
        warehouseRepository.delete(warehouse);
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Warehouse deleted");
        res.setData("Warehouse deleted successfully");
        return res;
    } 
 }
