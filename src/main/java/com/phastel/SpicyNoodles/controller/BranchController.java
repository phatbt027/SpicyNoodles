package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Branch;
import com.phastel.SpicyNoodles.service.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard/branches")
public class BranchController {
    
    private static final Logger logger = LoggerFactory.getLogger(BranchController.class);
    
    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public String branchManagement(Model model) {
        logger.info("Accessing branch management page");
        model.addAttribute("branches", branchService.getAllBranches());
        return "branch-management";
    }

    @GetMapping("/search")
    public String searchBranches(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String status,
            Model model) {
        
        logger.info("Searching branches with filters - address: {}, owner: {}, status: {}", 
            address, owner, status);

        List<Branch> branches;
        if (address != null && !address.isEmpty()) {
            branches = branchService.searchBranchesByAddress(address);
        } else if (owner != null && !owner.isEmpty()) {
            branches = branchService.getBranchesByOwner(owner);
        } else if (status != null && !status.isEmpty()) {
            boolean isEnabled = Boolean.parseBoolean(status);
            branches = isEnabled ? branchService.getEnabledBranches() : branchService.getAllBranches();
        } else {
            branches = branchService.getAllBranches();
        }

        model.addAttribute("branches", branches);
        return "branch-management";
    }

    @PostMapping
    public String createBranch(@ModelAttribute Branch branch, @RequestParam(required = false) String isEnabled) {
        logger.info("Creating new branch: {}", branch.getAddress());
        try {
            branch.setEnabled(isEnabled != null);
            branchService.createBranch(branch);
            logger.info("Successfully created branch: {}", branch.getAddress());
        } catch (Exception e) {
            logger.error("Error creating branch: {}", branch.getAddress(), e);
        }
        return "redirect:/dashboard/branches";
    }

    @PostMapping("/{id}/update")
    public String updateBranch(@PathVariable Long id, @ModelAttribute Branch branch, @RequestParam(required = false) String isEnabled) {
        logger.info("Updating branch with ID: {}", id);
        try {
            branch.setId(id);
            branch.setEnabled(isEnabled != null);
            branchService.updateBranch(branch);
            logger.info("Successfully updated branch with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error updating branch with ID: {}", id, e);
        }
        return "redirect:/dashboard/branches";
    }

    @PostMapping("/{id}/delete")
    public String deleteBranch(@PathVariable Long id) {
        logger.info("Deleting branch with ID: {}", id);
        try {
            branchService.deleteBranch(id);
            logger.info("Successfully deleted branch with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting branch with ID: {}", id, e);
        }
        return "redirect:/dashboard/branches";
    }
} 