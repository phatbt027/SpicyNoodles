package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Storage;
import com.phastel.SpicyNoodles.entity.StorageId;
import com.phastel.SpicyNoodles.entity.Branch;
import com.phastel.SpicyNoodles.entity.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StorageRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    private Branch branch;
    private Ingredient ingredient;
    private Storage storage;

    @BeforeEach
    void setUp() {
        branch = new Branch();
        branch.setAddress("456 Main St");
        branch.setPhoneNumber("9876543210");
        branch.setOwner("owner2");
        branch.setEnabled(true);
        branch = branchRepository.save(branch);

        ingredient = new Ingredient();
        ingredient.setName("Pepper");
        ingredient.setCategory(com.phastel.SpicyNoodles.entity.IngredientCategory.SPICE);
        ingredient = ingredientRepository.save(ingredient);

        storage = new Storage();
        storage.setBranch(branch);
        storage.setIngredient(ingredient);
        storage.setQuantity(10);
        storage.setExpirationDate(LocalDateTime.now().plusDays(10));
        storage.setId(new StorageId(branch.getId(), ingredient.getId()));
    }

    @Test
    void whenSaveStorage_thenStorageIsSaved() {
        Storage saved = storageRepository.save(storage);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getBranch().getId()).isEqualTo(branch.getId());
        assertThat(saved.getIngredient().getId()).isEqualTo(ingredient.getId());
    }

    @Test
    void whenFindByBranchId_thenReturnStorage() {
        entityManager.persist(storage);
        entityManager.flush();
        List<Storage> found = storageRepository.findByBranchId(branch.getId());
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getBranch().getId()).isEqualTo(branch.getId());
    }

    @Test
    void whenFindByIngredientId_thenReturnStorage() {
        entityManager.persist(storage);
        entityManager.flush();
        List<Storage> found = storageRepository.findByIngredientId(ingredient.getId());
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getIngredient().getId()).isEqualTo(ingredient.getId());
    }

    @Test
    void whenFindByExpirationDateBefore_thenReturnStorage() {
        storage.setExpirationDate(LocalDateTime.now().minusDays(1));
        entityManager.persist(storage);
        entityManager.flush();
        List<Storage> found = storageRepository.findByExpirationDateBefore(LocalDateTime.now());
        assertThat(found).isNotEmpty();
    }

    @Test
    void whenFindByBranchIdAndIngredientId_thenReturnStorage() {
        entityManager.persist(storage);
        entityManager.flush();
        List<Storage> found = storageRepository.findByBranchIdAndIngredientId(branch.getId(), ingredient.getId());
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getBranch().getId()).isEqualTo(branch.getId());
        assertThat(found.get(0).getIngredient().getId()).isEqualTo(ingredient.getId());
    }

    @Test
    void whenDeleteStorage_thenStorageIsRemoved() {
        Storage saved = entityManager.persist(storage);
        entityManager.flush();
        storageRepository.deleteById(saved.getId());
        assertThat(storageRepository.findById(saved.getId())).isEmpty();
    }
} 