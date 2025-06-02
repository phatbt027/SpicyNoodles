package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Branch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BranchRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private BranchRepository branchRepository;

    private Branch branch;

    @BeforeEach
    void setUp() {
        branch = new Branch();
        branch.setAddress("123 Main St");
        branch.setPhoneNumber("1234567890");
        branch.setOwner("owner1");
        branch.setEnabled(true);
    }

    @Test
    void whenSaveBranch_thenBranchIsSaved() {
        Branch saved = branchRepository.save(branch);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAddress()).isEqualTo(branch.getAddress());
    }

    @Test
    void whenFindByOwner_thenReturnBranch() {
        entityManager.persist(branch);
        entityManager.flush();
        List<Branch> found = branchRepository.findByOwner("owner1");
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getOwner()).isEqualTo("owner1");
    }

    @Test
    void whenFindByIsEnabled_thenReturnBranch() {
        entityManager.persist(branch);
        entityManager.flush();
        List<Branch> found = branchRepository.findByIsEnabled(true);
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).isEnabled()).isTrue();
    }

    @Test
    void whenFindByAddressContainingIgnoreCase_thenReturnBranch() {
        entityManager.persist(branch);
        entityManager.flush();
        List<Branch> found = branchRepository.findByAddressContainingIgnoreCase("main");
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getAddress()).containsIgnoringCase("main");
    }

    @Test
    void whenDeleteBranch_thenBranchIsRemoved() {
        Branch saved = entityManager.persist(branch);
        entityManager.flush();
        branchRepository.deleteById(saved.getId());
        assertThat(branchRepository.findById(saved.getId())).isEmpty();
    }
} 