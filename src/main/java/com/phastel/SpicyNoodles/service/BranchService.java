package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Branch;
import java.util.List;

public interface BranchService {
    Branch createBranch(Branch branch);
    Branch updateBranch(Branch branch);
    void deleteBranch(Long id);
    Branch getBranchById(Long id);
    List<Branch> getAllBranches();
    List<Branch> getBranchesByOwner(String owner);
    List<Branch> getEnabledBranches();
    List<Branch> searchBranchesByAddress(String address);
    Branch toggleBranchStatus(Long id);
} 