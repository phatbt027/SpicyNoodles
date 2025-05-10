package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Storage;
import com.phastel.SpicyNoodles.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/storages")
public class StorageController {

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    public ResponseEntity<Storage> createStorage(@RequestBody Storage storage) {
        return ResponseEntity.ok(storageService.createStorage(storage));
    }

    @PutMapping("/{materialId}/{branchId}")
    public ResponseEntity<Storage> updateStorage(
            @PathVariable Long materialId,
            @PathVariable Long branchId,
            @RequestBody Storage storage) {
        return ResponseEntity.ok(storageService.updateStorage(storage));
    }

    @DeleteMapping("/{materialId}/{branchId}")
    public ResponseEntity<Void> deleteStorage(
            @PathVariable Long materialId,
            @PathVariable Long branchId) {
        storageService.deleteStorage(materialId, branchId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{materialId}/{branchId}")
    public ResponseEntity<Storage> getStorageById(
            @PathVariable Long materialId,
            @PathVariable Long branchId) {
        try {
            return ResponseEntity.ok(storageService.getStorageById(materialId, branchId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Storage>> getAllStorages() {
        return ResponseEntity.ok(storageService.getAllStorages());
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<Storage>> getStoragesByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(storageService.getStoragesByBranch(branchId));
    }

    @GetMapping("/material/{materialId}")
    public ResponseEntity<List<Storage>> getStoragesByMaterial(@PathVariable Long materialId) {
        return ResponseEntity.ok(storageService.getStoragesByMaterial(materialId));
    }

    @GetMapping("/expired")
    public ResponseEntity<List<Storage>> getExpiredStorages(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(storageService.getExpiredStorages(date));
    }

    @PatchMapping("/{materialId}/{branchId}/quantity")
    public ResponseEntity<Storage> updateStorageQuantity(
            @PathVariable Long materialId,
            @PathVariable Long branchId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(storageService.updateStorageQuantity(materialId, branchId, quantity));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }
} 