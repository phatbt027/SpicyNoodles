package com.phastel.SpicyNoodles.controller;

import com.phastel.SpicyNoodles.entity.Branch;
import com.phastel.SpicyNoodles.service.BranchService;
import com.phastel.SpicyNoodles.exception.RestExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BranchControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BranchService branchService;

    @InjectMocks
    private BranchController branchController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(branchController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    public void createBranch_ShouldReturnCreatedBranch() throws Exception {
        Branch branch = new Branch();
        branch.setAddress("123 Main St");
        branch.setPhoneNumber("123-456-7890");
        branch.setOwner("John Doe");

        when(branchService.createBranch(any(Branch.class))).thenReturn(branch);

        mockMvc.perform(post("/api/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.phoneNumber").value("123-456-7890"));
    }

    @Test
    public void getBranchById_ShouldReturnBranch() throws Exception {
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setAddress("123 Main St");

        when(branchService.getBranchById(1L)).thenReturn(branch);

        mockMvc.perform(get("/api/branches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }

    @Test
    public void getAllBranches_ShouldReturnAllBranches() throws Exception {
        Branch branch1 = new Branch();
        branch1.setId(1L);
        branch1.setAddress("123 Main St");

        Branch branch2 = new Branch();
        branch2.setId(2L);
        branch2.setAddress("456 Oak St");

        List<Branch> branches = Arrays.asList(branch1, branch2);

        when(branchService.getAllBranches()).thenReturn(branches);

        mockMvc.perform(get("/api/branches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].address").value("123 Main St"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].address").value("456 Oak St"));
    }

    @Test
    public void updateBranch_ShouldReturnUpdatedBranch() throws Exception {
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setAddress("Updated Address");
        branch.setPhoneNumber("987-654-3210");

        when(branchService.updateBranch(any(Branch.class))).thenReturn(branch);

        mockMvc.perform(put("/api/branches/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(branch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.address").value("Updated Address"));
    }

    @Test
    public void deleteBranch_ShouldReturnOk() throws Exception {
        doNothing().when(branchService).deleteBranch(1L);

        mockMvc.perform(delete("/api/branches/1"))
                .andExpect(status().isOk());

        verify(branchService, times(1)).deleteBranch(1L);
    }

    @Test
    public void getBranchById_WhenBranchNotFound_ShouldReturnNotFound() throws Exception {
        when(branchService.getBranchById(1L))
                .thenThrow(new IllegalArgumentException("Branch not found with id: 1"));

        mockMvc.perform(get("/api/branches/1"))
                .andExpect(status().isNotFound());
    }
} 