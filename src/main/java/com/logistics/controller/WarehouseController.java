package com.logistics.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.Warehouse;
import com.logistics.service.WarehouseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

 // 1. Create Warehouse
    @PostMapping
    public ResponseEntity<ResponseStructure<Warehouse>> createWarehouse(
            @RequestBody Warehouse warehouse) {
        return new ResponseEntity<>(
                warehouseService.createWarehouse(warehouse),
                HttpStatus.CREATED
        );
    }

    // 2. Get All Warehouses
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Warehouse>>> getAllWarehouses() {
        return new ResponseEntity<>(
                warehouseService.getAllWarehouses(),
                HttpStatus.OK
        );
    }

    // 3. Get By Id
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Warehouse>> getById(
            @PathVariable Long id) {
        return new ResponseEntity<>(
                warehouseService.getById(id),
                HttpStatus.OK
        );
    }

    // 4. Get By Location
    @GetMapping("/location/{location}")
    public ResponseEntity<ResponseStructure<List<Warehouse>>> getByLocation(
            @PathVariable String location) {
        return new ResponseEntity<>(
                warehouseService.getByLocation(location),
                HttpStatus.OK
        );
    }

    // 5. Get By Contact No
    @GetMapping("/contact/{contact}")
    public ResponseEntity<ResponseStructure<Warehouse>> getByContact(
            @PathVariable String contact) {
        return new ResponseEntity<>(
                warehouseService.getByContact(contact),
                HttpStatus.OK
        );
    }

    // 6. Update Warehouse
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<Warehouse>> updateWarehouse(
            @PathVariable Long id,
            @RequestBody Warehouse warehouse) {
        return new ResponseEntity<>(
                warehouseService.updateWarehouse(id, warehouse),
                HttpStatus.OK
        );
    }

    // 7. Delete Warehouse
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteWarehouse(
            @PathVariable Long id) {
        return new ResponseEntity<>(
                warehouseService.deleteWarehouse(id),
                HttpStatus.OK
        );
    }
}
