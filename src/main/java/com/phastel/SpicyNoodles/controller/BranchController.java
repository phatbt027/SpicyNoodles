package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Branch;
import com.phastel.SpicyNoodles.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping
    public ResponseEntity<Branch> createBranch(@RequestBody Branch branch) {
        return ResponseEntity.ok(branchService.createBranch(branch));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id, @RequestBody Branch branch) {
        branch.setId(id);
        return ResponseEntity.ok(branchService.updateBranch(branch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @GetMapping
    public ResponseEntity<List<Branch>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @GetMapping("/owner/{owner}")
    public ResponseEntity<List<Branch>> getBranchesByOwner(@PathVariable String owner) {
        return ResponseEntity.ok(branchService.getBranchesByOwner(owner));
    }

    @GetMapping("/enabled")
    public ResponseEntity<List<Branch>> getEnabledBranches() {
        return ResponseEntity.ok(branchService.getEnabledBranches());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Branch>> searchBranchesByAddress(@RequestParam String address) {
        return ResponseEntity.ok(branchService.searchBranchesByAddress(address));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Branch> toggleBranchStatus(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.toggleBranchStatus(id));
    }
} 