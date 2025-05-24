package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Storage;
import com.phastel.SpicyNoodles.entity.StorageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<Storage, StorageId> {
    List<Storage> findByBranchId(Long branchId);
    List<Storage> findByIngredientId(Long ingredientId);
    List<Storage> findByExpirationDateBefore(LocalDateTime date);
    List<Storage> findByBranchIdAndIngredientId(Long branchId, Long ingredientId);
} 