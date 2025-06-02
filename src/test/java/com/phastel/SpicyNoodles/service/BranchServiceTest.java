package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Branch;
import com.phastel.SpicyNoodles.repository.BranchRepository;
import com.phastel.SpicyNoodles.service.impl.BranchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchServiceImpl branchService;

    private Branch testBranch;

    @BeforeEach
    void setUp() {
        testBranch = new Branch();
        testBranch.setId(1L);
        testBranch.setAddress("123 Test St");
        testBranch.setPhoneNumber("1234567890");
        testBranch.setOwner("Test Owner");
        testBranch.setEnabled(true);
    }

    @Test
    void whenCreateBranch_thenBranchIsCreated() {
        // Given
        when(branchRepository.save(any(Branch.class))).thenReturn(testBranch);

        // When
        Branch createdBranch = branchService.createBranch(testBranch);

        // Then
        assertThat(createdBranch).isNotNull();
        assertThat(createdBranch.getAddress()).isEqualTo(testBranch.getAddress());
        verify(branchRepository).save(any(Branch.class));
    }

    @Test
    void whenUpdateBranch_thenBranchIsUpdated() {
        // Given
        when(branchRepository.existsById(testBranch.getId())).thenReturn(true);
        when(branchRepository.findById(testBranch.getId())).thenReturn(Optional.of(testBranch));
        when(branchRepository.save(any(Branch.class))).thenReturn(testBranch);

        // When
        Branch updatedBranch = branchService.updateBranch(testBranch);

        // Then
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getAddress()).isEqualTo(testBranch.getAddress());
        verify(branchRepository).save(any(Branch.class));
    }

    @Test
    void whenUpdateNonExistentBranch_thenThrowException() {
        // Given
        when(branchRepository.existsById(testBranch.getId())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> branchService.updateBranch(testBranch))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Branch not found");
    }

    @Test
    void whenDeleteBranch_thenBranchIsDeleted() {
        // Given
        when(branchRepository.existsById(testBranch.getId())).thenReturn(true);

        // When
        branchService.deleteBranch(testBranch.getId());

        // Then
        verify(branchRepository).deleteById(testBranch.getId());
    }

    @Test
    void whenDeleteNonExistentBranch_thenThrowException() {
        // Given
        when(branchRepository.existsById(testBranch.getId())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> branchService.deleteBranch(testBranch.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Branch not found");
    }

    @Test
    void whenGetBranchById_thenReturnBranch() {
        // Given
        when(branchRepository.findById(testBranch.getId())).thenReturn(Optional.of(testBranch));

        // When
        Branch foundBranch = branchService.getBranchById(testBranch.getId());

        // Then
        assertThat(foundBranch).isNotNull();
        assertThat(foundBranch.getId()).isEqualTo(testBranch.getId());
    }

    @Test
    void whenGetNonExistentBranchById_thenThrowException() {
        // Given
        when(branchRepository.findById(testBranch.getId())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> branchService.getBranchById(testBranch.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Branch not found");
    }

    @Test
    void whenGetAllBranches_thenReturnAllBranches() {
        // Given
        List<Branch> branches = Arrays.asList(testBranch);
        when(branchRepository.findAll()).thenReturn(branches);

        // When
        List<Branch> foundBranches = branchService.getAllBranches();

        // Then
        assertThat(foundBranches).isNotEmpty();
        assertThat(foundBranches).hasSize(1);
        assertThat(foundBranches.get(0).getAddress()).isEqualTo(testBranch.getAddress());
    }

    @Test
    void whenGetBranchesByOwner_thenReturnBranches() {
        // Given
        List<Branch> branches = Arrays.asList(testBranch);
        when(branchRepository.findByOwner(testBranch.getOwner())).thenReturn(branches);

        // When
        List<Branch> foundBranches = branchService.getBranchesByOwner(testBranch.getOwner());

        // Then
        assertThat(foundBranches).isNotEmpty();
        assertThat(foundBranches.get(0).getOwner()).isEqualTo(testBranch.getOwner());
    }

    @Test
    void whenGetEnabledBranches_thenReturnEnabledBranches() {
        // Given
        List<Branch> branches = Arrays.asList(testBranch);
        when(branchRepository.findByIsEnabled(true)).thenReturn(branches);

        // When
        List<Branch> foundBranches = branchService.getEnabledBranches();

        // Then
        assertThat(foundBranches).isNotEmpty();
        assertThat(foundBranches.get(0).isEnabled()).isTrue();
    }

    @Test
    void whenSearchBranchesByAddress_thenReturnMatchingBranches() {
        // Given
        List<Branch> branches = Arrays.asList(testBranch);
        when(branchRepository.findByAddressContainingIgnoreCase("test")).thenReturn(branches);

        // When
        List<Branch> foundBranches = branchService.searchBranchesByAddress("test");

        // Then
        assertThat(foundBranches).isNotEmpty();
        assertThat(foundBranches.get(0).getAddress()).containsIgnoringCase("test");
    }

    @Test
    void whenToggleBranchStatus_thenStatusIsToggled() {
        // Given
        when(branchRepository.findById(testBranch.getId())).thenReturn(Optional.of(testBranch));
        when(branchRepository.save(any(Branch.class))).thenReturn(testBranch);

        // When
        Branch toggledBranch = branchService.toggleBranchStatus(testBranch.getId());

        // Then
        assertThat(toggledBranch).isNotNull();
        assertThat(toggledBranch.isEnabled()).isFalse();
        verify(branchRepository).save(any(Branch.class));
    }
} 