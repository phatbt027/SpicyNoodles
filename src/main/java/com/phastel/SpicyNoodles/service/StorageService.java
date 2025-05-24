package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Storage;
import java.time.LocalDateTime;
import java.util.List;

public interface StorageService {
    Storage createStorage(Storage storage);
    Storage updateStorage(Storage storage);
    void deleteStorage(Long materialId, Long branchId);
    Storage getStorageById(Long materialId, Long branchId);
    List<Storage> getAllStorages();
    List<Storage> getStoragesByBranch(Long branchId);
    List<Storage> getStoragesByIngredient(Long materialId);
    List<Storage> getExpiredStorages(LocalDateTime date);
    Storage updateStorageQuantity(Long materialId, Long branchId, Integer quantity);
} 