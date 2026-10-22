package com.logistics.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.logistics.dto.ResponseStructure;
import com.logistics.entity.PackageEntity;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.PackageEntityRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service layer for PackageEntity CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageEntityRepository packageRepository;

    // 1. get all packageEntity
    public ResponseStructure<List<PackageEntity>> getAllPackages() {
        List<PackageEntity> list = packageRepository.findAll();
        ResponseStructure<List<PackageEntity>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Packages retrieved");
        res.setData(list);
        return res;
    }

    // 2.get a package by its id
    public ResponseStructure<PackageEntity> getPackageById(Long id) {
        PackageEntity pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
        ResponseStructure<PackageEntity> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Package retrieved");
        res.setData(pkg);
        return res;
    }
 // 3. Get packages by their type
    public ResponseStructure<List<PackageEntity>> getByPackageType(
            com.logistics.enums.PackageType packageType) {
        List<PackageEntity> list = packageRepository.findByPackageType(packageType);
        ResponseStructure<List<PackageEntity>> res = new ResponseStructure<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Packages retrieved by type");
        res.setData(list);
        return res;
    }
}
