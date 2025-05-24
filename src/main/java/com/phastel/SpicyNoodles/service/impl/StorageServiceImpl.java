package com.phastel.SpicyNoodles.service.impl;

import com.phastel.SpicyNoodles.entity.Storage;
import com.phastel.SpicyNoodles.entity.StorageId;
import com.phastel.SpicyNoodles.repository.StorageRepository;
import com.phastel.SpicyNoodles.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;

    @Autowired
    public StorageServiceImpl(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @Override
    public Storage createStorage(Storage storage) {
        return storageRepository.save(storage);
    }

    @Override
    public Storage updateStorage(Storage storage) {
        StorageId id = new StorageId(storage.getIngredient().getId(), storage.getBranch().getId());
        if (!storageRepository.existsById(id)) {
            throw new IllegalArgumentException("Storage not found");
        }
        return storageRepository.save(storage);
    }

    @Override
    public void deleteStorage(Long ingredientId, Long branchId) {
        StorageId id = new StorageId(ingredientId, branchId);
        if (!storageRepository.existsById(id)) {
            throw new IllegalArgumentException("Storage not found");
        }
        storageRepository.deleteById(id);
    }

    @Override
    public Storage getStorageById(Long ingredientId, Long branchId) {
        StorageId id = new StorageId(ingredientId, branchId);
        return storageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Storage not found"));
    }

    @Override
    public List<Storage> getAllStorages() {
        return storageRepository.findAll();
    }

    @Override
    public List<Storage> getStoragesByBranch(Long branchId) {
        return storageRepository.findByBranchId(branchId);
    }

    @Override
    public List<Storage> getStoragesByIngredient(Long ingredientId) {
        return storageRepository.findByIngredientId(ingredientId);
    }

    @Override
    public List<Storage> getExpiredStorages(LocalDateTime date) {
        return storageRepository.findByExpirationDateBefore(date);
    }

    @Override
    public Storage updateStorageQuantity(Long ingredientId, Long branchId, Integer quantity) {
        Storage storage = getStorageById(ingredientId, branchId);
        storage.setQuantity(quantity);
        return storageRepository.save(storage);
    }
} 