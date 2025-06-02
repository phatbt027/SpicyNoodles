package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Ingredient;
import com.phastel.SpicyNoodles.entity.IngredientCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class IngredientRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private IngredientRepository ingredientRepository;

    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        ingredient = new Ingredient();
        ingredient.setName("Chili");
        ingredient.setCategory(IngredientCategory.SPICE);
    }

    @Test
    void whenSaveIngredient_thenIngredientIsSaved() {
        Ingredient saved = ingredientRepository.save(ingredient);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(ingredient.getName());
    }

    @Test
    void whenFindByCategory_thenReturnIngredient() {
        // Save the ingredient and verify it was saved
        Ingredient saved = entityManager.persist(ingredient);
        entityManager.flush();
        
        // Verify the ingredient was saved with correct category
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCategory()).isEqualTo(IngredientCategory.SPICE);
        
        // Try to find by category using enum
        List<Ingredient> found = ingredientRepository.findByCategory(IngredientCategory.SPICE);
        
        // Add detailed assertions
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getCategory()).isEqualTo(IngredientCategory.SPICE);
        assertThat(found.get(0).getName()).isEqualTo("Chili");
    }

    @Test
    void whenFindByNameContainingIgnoreCase_thenReturnIngredient() {
        entityManager.persist(ingredient);
        entityManager.flush();
        List<Ingredient> found = ingredientRepository.findByNameContainingIgnoreCase("chili");
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getName()).containsIgnoringCase("chili");
    }

    @Test
    void whenDeleteIngredient_thenIngredientIsRemoved() {
        Ingredient saved = entityManager.persist(ingredient);
        entityManager.flush();
        ingredientRepository.deleteById(saved.getId());
        assertThat(ingredientRepository.findById(saved.getId())).isEmpty();
    }
} 