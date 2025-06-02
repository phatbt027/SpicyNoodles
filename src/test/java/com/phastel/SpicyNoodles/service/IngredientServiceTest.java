package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Ingredient;
import com.phastel.SpicyNoodles.entity.IngredientCategory;
import com.phastel.SpicyNoodles.repository.IngredientRepository;
import com.phastel.SpicyNoodles.service.impl.IngredientServiceImpl;
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
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientServiceImpl ingredientService;

    private Ingredient testIngredient;

    @BeforeEach
    void setUp() {
        testIngredient = new Ingredient();
        testIngredient.setId(1L);
        testIngredient.setName("Test Ingredient");
        testIngredient.setCategory(IngredientCategory.SPICE);
        testIngredient.setDescription("Test Description");
    }

    @Test
    void whenCreateIngredient_thenIngredientIsCreated() {
        // Given
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(testIngredient);

        // When
        Ingredient createdIngredient = ingredientService.createIngredient(testIngredient);

        // Then
        assertThat(createdIngredient).isNotNull();
        assertThat(createdIngredient.getName()).isEqualTo(testIngredient.getName());
        verify(ingredientRepository).save(any(Ingredient.class));
    }

    @Test
    void whenUpdateIngredient_thenIngredientIsUpdated() {
        // Given
        when(ingredientRepository.existsById(testIngredient.getId())).thenReturn(true);
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(testIngredient);

        // When
        Ingredient updatedIngredient = ingredientService.updateIngredient(testIngredient);

        // Then
        assertThat(updatedIngredient).isNotNull();
        assertThat(updatedIngredient.getName()).isEqualTo(testIngredient.getName());
        verify(ingredientRepository).save(any(Ingredient.class));
    }

    @Test
    void whenUpdateNonExistentIngredient_thenThrowException() {
        // Given
        when(ingredientRepository.existsById(testIngredient.getId())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> ingredientService.updateIngredient(testIngredient))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ingredient not found");
    }

    @Test
    void whenDeleteIngredient_thenIngredientIsDeleted() {
        // Given
        when(ingredientRepository.existsById(testIngredient.getId())).thenReturn(true);

        // When
        ingredientService.deleteIngredient(testIngredient.getId());

        // Then
        verify(ingredientRepository).deleteById(testIngredient.getId());
    }

    @Test
    void whenDeleteNonExistentIngredient_thenThrowException() {
        // Given
        when(ingredientRepository.existsById(testIngredient.getId())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> ingredientService.deleteIngredient(testIngredient.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ingredient not found");
    }

    @Test
    void whenGetIngredientById_thenReturnIngredient() {
        // Given
        when(ingredientRepository.findById(testIngredient.getId())).thenReturn(Optional.of(testIngredient));

        // When
        Ingredient foundIngredient = ingredientService.getIngredientById(testIngredient.getId());

        // Then
        assertThat(foundIngredient).isNotNull();
        assertThat(foundIngredient.getId()).isEqualTo(testIngredient.getId());
    }

    @Test
    void whenGetNonExistentIngredientById_thenThrowException() {
        // Given
        when(ingredientRepository.findById(testIngredient.getId())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> ingredientService.getIngredientById(testIngredient.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ingredient not found");
    }

    @Test
    void whenGetAllIngredients_thenReturnAllIngredients() {
        // Given
        List<Ingredient> ingredients = Arrays.asList(testIngredient);
        when(ingredientRepository.findAll()).thenReturn(ingredients);

        // When
        List<Ingredient> foundIngredients = ingredientService.getAllIngredients();

        // Then
        assertThat(foundIngredients).isNotEmpty();
        assertThat(foundIngredients).hasSize(1);
        assertThat(foundIngredients.get(0).getName()).isEqualTo(testIngredient.getName());
    }

    @Test
    void whenSearchIngredientsByName_thenReturnMatchingIngredients() {
        // Given
        List<Ingredient> ingredients = Arrays.asList(testIngredient);
        when(ingredientRepository.findByNameContainingIgnoreCase("test")).thenReturn(ingredients);

        // When
        List<Ingredient> foundIngredients = ingredientService.searchIngredientsByName("test");

        // Then
        assertThat(foundIngredients).isNotEmpty();
        assertThat(foundIngredients.get(0).getName()).containsIgnoringCase("test");
    }

    @Test
    void whenGetIngredientsByCategory_thenReturnMatchingIngredients() {
        // Given
        List<Ingredient> ingredients = Arrays.asList(testIngredient);
        when(ingredientRepository.findByCategory(IngredientCategory.SPICE)).thenReturn(ingredients);

        // When
        List<Ingredient> foundIngredients = ingredientService.getIngredientsByCategory("SPICE");

        // Then
        assertThat(foundIngredients).isNotEmpty();
        assertThat(foundIngredients.get(0).getCategory()).isEqualTo(IngredientCategory.SPICE);
    }

    @Test
    void whenGetIngredientsByInvalidCategory_thenThrowException() {
        // When/Then
        assertThatThrownBy(() -> ingredientService.getIngredientsByCategory("INVALID_CATEGORY"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid category");
    }
} 