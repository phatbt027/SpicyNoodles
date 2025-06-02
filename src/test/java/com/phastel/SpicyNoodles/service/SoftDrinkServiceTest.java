package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.SoftDrink;
import com.phastel.SpicyNoodles.entity.Ingredient;
import com.phastel.SpicyNoodles.entity.SoftDrinkIngredient;
import com.phastel.SpicyNoodles.repository.SoftDrinkRepository;
import com.phastel.SpicyNoodles.repository.IngredientRepository;
import com.phastel.SpicyNoodles.service.impl.SoftDrinkServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SoftDrinkServiceTest {

    @Mock
    private SoftDrinkRepository softDrinkRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private SoftDrinkServiceImpl softDrinkService;

    private SoftDrink testSoftDrink;
    private Ingredient testIngredient;

    @BeforeEach
    void setUp() {
        testSoftDrink = new SoftDrink();
        testSoftDrink.setId(1L);
        testSoftDrink.setName("Test Drink");
        testSoftDrink.setDescription("Test Description");
        testSoftDrink.setPrice(new BigDecimal("2.50"));
        testSoftDrink.setAvailable(true);
        testSoftDrink.setSize("330ml");
        testSoftDrink.setHot(false);
        testSoftDrink.setIngredients(new HashSet<>());

        testIngredient = new Ingredient();
        testIngredient.setId(1L);
        testIngredient.setName("Test Ingredient");
    }

    @Test
    void whenCreateSoftDrink_thenSoftDrinkIsCreated() {
        // Given
        when(softDrinkRepository.save(any(SoftDrink.class))).thenReturn(testSoftDrink);

        // When
        SoftDrink createdSoftDrink = softDrinkService.createSoftDrink(testSoftDrink);

        // Then
        assertThat(createdSoftDrink).isNotNull();
        assertThat(createdSoftDrink.getName()).isEqualTo(testSoftDrink.getName());
        verify(softDrinkRepository).save(any(SoftDrink.class));
    }

    @Test
    void whenUpdateSoftDrink_thenSoftDrinkIsUpdated() {
        // Given
        when(softDrinkRepository.existsById(testSoftDrink.getId())).thenReturn(true);
        when(softDrinkRepository.save(any(SoftDrink.class))).thenReturn(testSoftDrink);

        // When
        SoftDrink updatedSoftDrink = softDrinkService.updateSoftDrink(testSoftDrink);

        // Then
        assertThat(updatedSoftDrink).isNotNull();
        assertThat(updatedSoftDrink.getName()).isEqualTo(testSoftDrink.getName());
        verify(softDrinkRepository).save(any(SoftDrink.class));
    }

    @Test
    void whenUpdateNonExistentSoftDrink_thenThrowException() {
        // Given
        when(softDrinkRepository.existsById(testSoftDrink.getId())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> softDrinkService.updateSoftDrink(testSoftDrink))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Soft drink not found");
    }

    @Test
    void whenDeleteSoftDrink_thenSoftDrinkIsDeleted() {
        // Given
        when(softDrinkRepository.existsById(testSoftDrink.getId())).thenReturn(true);

        // When
        softDrinkService.deleteSoftDrink(testSoftDrink.getId());

        // Then
        verify(softDrinkRepository).deleteById(testSoftDrink.getId());
    }

    @Test
    void whenDeleteNonExistentSoftDrink_thenThrowException() {
        // Given
        when(softDrinkRepository.existsById(testSoftDrink.getId())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> softDrinkService.deleteSoftDrink(testSoftDrink.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Soft drink not found");
    }

    @Test
    void whenGetSoftDrinkById_thenReturnSoftDrink() {
        // Given
        when(softDrinkRepository.findById(testSoftDrink.getId())).thenReturn(Optional.of(testSoftDrink));

        // When
        SoftDrink foundSoftDrink = softDrinkService.getSoftDrinkById(testSoftDrink.getId());

        // Then
        assertThat(foundSoftDrink).isNotNull();
        assertThat(foundSoftDrink.getId()).isEqualTo(testSoftDrink.getId());
    }

    @Test
    void whenGetNonExistentSoftDrinkById_thenThrowException() {
        // Given
        when(softDrinkRepository.findById(testSoftDrink.getId())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> softDrinkService.getSoftDrinkById(testSoftDrink.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Soft drink not found");
    }

    @Test
    void whenGetAllSoftDrinks_thenReturnAllSoftDrinks() {
        // Given
        List<SoftDrink> softDrinks = Arrays.asList(testSoftDrink);
        when(softDrinkRepository.findAll()).thenReturn(softDrinks);

        // When
        List<SoftDrink> foundSoftDrinks = softDrinkService.getAllSoftDrinks();

        // Then
        assertThat(foundSoftDrinks).isNotEmpty();
        assertThat(foundSoftDrinks).hasSize(1);
        assertThat(foundSoftDrinks.get(0).getName()).isEqualTo(testSoftDrink.getName());
    }

    @Test
    void whenSearchSoftDrinksByName_thenReturnMatchingSoftDrinks() {
        // Given
        List<SoftDrink> softDrinks = Arrays.asList(testSoftDrink);
        when(softDrinkRepository.findByNameContainingIgnoreCase("test")).thenReturn(softDrinks);

        // When
        List<SoftDrink> foundSoftDrinks = softDrinkService.searchSoftDrinksByName("test");

        // Then
        assertThat(foundSoftDrinks).isNotEmpty();
        assertThat(foundSoftDrinks.get(0).getName()).containsIgnoringCase("test");
    }

    @Test
    void whenGetSoftDrinksByPriceRange_thenReturnMatchingSoftDrinks() {
        // Given
        List<SoftDrink> softDrinks = Arrays.asList(testSoftDrink);
        when(softDrinkRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(
            new BigDecimal("2.00"), new BigDecimal("3.00"))).thenReturn(softDrinks);

        // When
        List<SoftDrink> foundSoftDrinks = softDrinkService.getSoftDrinksByPriceRange(
            new BigDecimal("2.00"), new BigDecimal("3.00"));

        // Then
        assertThat(foundSoftDrinks).isNotEmpty();
        assertThat(foundSoftDrinks.get(0).getPrice()).isBetween(
            new BigDecimal("2.00"), new BigDecimal("3.00"));
    }

    @Test
    void whenAddIngredientToSoftDrink_thenIngredientIsAdded() {
        // Given
        when(softDrinkRepository.findById(testSoftDrink.getId())).thenReturn(Optional.of(testSoftDrink));
        when(ingredientRepository.findById(testIngredient.getId())).thenReturn(Optional.of(testIngredient));
        when(softDrinkRepository.save(any(SoftDrink.class))).thenReturn(testSoftDrink);

        // When
        SoftDrink updatedSoftDrink = softDrinkService.addIngredientToSoftDrink(
            testSoftDrink.getId(), testIngredient.getId(), 1.0);

        // Then
        assertThat(updatedSoftDrink).isNotNull();
        verify(softDrinkRepository).save(any(SoftDrink.class));
    }

    @Test
    void whenAddNonExistentIngredientToSoftDrink_thenThrowException() {
        // Given
        when(softDrinkRepository.findById(testSoftDrink.getId())).thenReturn(Optional.of(testSoftDrink));
        when(ingredientRepository.findById(testIngredient.getId())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> softDrinkService.addIngredientToSoftDrink(
            testSoftDrink.getId(), testIngredient.getId(), 1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ingredient not found");
    }

    @Test
    void whenRemoveIngredientFromSoftDrink_thenIngredientIsRemoved() {
        // Given
        SoftDrinkIngredient softDrinkIngredient = new SoftDrinkIngredient();
        softDrinkIngredient.setSoftDrink(testSoftDrink);
        softDrinkIngredient.setIngredient(testIngredient);
        testSoftDrink.getIngredients().add(softDrinkIngredient);

        when(softDrinkRepository.findById(testSoftDrink.getId())).thenReturn(Optional.of(testSoftDrink));
        when(softDrinkRepository.save(any(SoftDrink.class))).thenReturn(testSoftDrink);

        // When
        SoftDrink updatedSoftDrink = softDrinkService.removeIngredientFromSoftDrink(
            testSoftDrink.getId(), testIngredient.getId());

        // Then
        assertThat(updatedSoftDrink).isNotNull();
        verify(softDrinkRepository).save(any(SoftDrink.class));
    }
} 