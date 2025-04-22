package com.phastel.SpicyNoodles.service.impl;

import com.phastel.SpicyNoodles.entity.Branch;
import com.phastel.SpicyNoodles.repository.BranchRepository;
import com.phastel.SpicyNoodles.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public Branch createBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    @Override
    public Branch updateBranch(Branch branch) {
        if (!branchRepository.existsById(branch.getId())) {
            throw new IllegalArgumentException("Branch not found with id: " + branch.getId());
        }
        return branchRepository.save(branch);
    }

    @Override
    public void deleteBranch(Long id) {
        if (!branchRepository.existsById(id)) {
            throw new IllegalArgumentException("Branch not found with id: " + id);
        }
        branchRepository.deleteById(id);
    }

    @Override
    public Branch getBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found with id: " + id));
    }

    @Override
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    @Override
    public List<Branch> getBranchesByOwner(String owner) {
        return branchRepository.findByOwner(owner);
    }

    @Override
    public List<Branch> getEnabledBranches() {
        return branchRepository.findByIsEnabled(true);
    }

    @Override
    public List<Branch> searchBranchesByAddress(String address) {
        return branchRepository.findByAddressContainingIgnoreCase(address);
    }

    @Override
    public Branch toggleBranchStatus(Long id) {
        Branch branch = getBranchById(id);
        branch.setEnabled(!branch.isEnabled());
        return branchRepository.save(branch);
    }
} 