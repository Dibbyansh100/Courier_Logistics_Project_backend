package com.logistics.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.PackageEntity;
import com.logistics.enums.PackageType;
import com.logistics.service.PackageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

 // 1. Get all Packages
    @GetMapping
    public ResponseEntity<ResponseStructure<List<PackageEntity>>> getAllPackages() {
        return new ResponseEntity<>(
                packageService.getAllPackages(),
                HttpStatus.OK
        );
    }

    // 2. Get Package by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<PackageEntity>> getPackageById(
            @PathVariable Long id) {
        return new ResponseEntity<>(
                packageService.getPackageById(id),
                HttpStatus.OK
        );
    }

    // 3. Get Packages by Type
    @GetMapping("/type/{type}")
    public ResponseEntity<ResponseStructure<List<PackageEntity>>> getByPackageType(
            @PathVariable PackageType type) {
        return new ResponseEntity<>(
                packageService.getByPackageType(type),
                HttpStatus.OK
        );
    }
}
