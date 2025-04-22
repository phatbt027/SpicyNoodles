package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByOwner(String owner);
    List<Branch> findByIsEnabled(boolean isEnabled);
    List<Branch> findByAddressContainingIgnoreCase(String address);
} 