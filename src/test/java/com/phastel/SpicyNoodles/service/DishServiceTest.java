package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Dish;
import com.phastel.SpicyNoodles.entity.Ingredient;
import com.phastel.SpicyNoodles.entity.DishIngredient;
import com.phastel.SpicyNoodles.repository.DishRepository;
import com.phastel.SpicyNoodles.repository.IngredientRepository;
import com.phastel.SpicyNoodles.service.impl.DishServiceImpl;
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
class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private DishServiceImpl dishService;

    private Dish testDish;
    private Ingredient testIngredient;

    @BeforeEach
    void setUp() {
        testDish = new Dish();
        testDish.setId(1L);
        testDish.setName("Test Dish");
        testDish.setDescription("Test Description");
        testDish.setPrice(new BigDecimal("9.99"));
        testDish.setCategory("MAIN_COURSE");
        testDish.setAvailable(true);
        testDish.setIngredients(new HashSet<>());

        testIngredient = new Ingredient();
        testIngredient.setId(1L);
        testIngredient.setName("Test Ingredient");
    }

    @Test
    void whenCreateDish_thenDishIsCreated() {
        // Given
        when(dishRepository.save(any(Dish.class))).thenReturn(testDish);

        // When
        Dish createdDish = dishService.createDish(testDish);

        // Then
        assertThat(createdDish).isNotNull();
        assertThat(createdDish.getName()).isEqualTo(testDish.getName());
        verify(dishRepository).save(any(Dish.class));
    }

    @Test
    void whenUpdateDish_thenDishIsUpdated() {
        // Given
        when(dishRepository.existsById(testDish.getId())).thenReturn(true);
        when(dishRepository.save(any(Dish.class))).thenReturn(testDish);

        // When
        Dish updatedDish = dishService.updateDish(testDish);

        // Then
        assertThat(updatedDish).isNotNull();
        assertThat(updatedDish.getName()).isEqualTo(testDish.getName());
        verify(dishRepository).save(any(Dish.class));
    }

    @Test
    void whenUpdateNonExistentDish_thenThrowException() {
        // Given
        when(dishRepository.existsById(testDish.getId())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> dishService.updateDish(testDish))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dish not found");
    }

    @Test
    void whenDeleteDish_thenDishIsDeleted() {
        // Given
        when(dishRepository.existsById(testDish.getId())).thenReturn(true);

        // When
        dishService.deleteDish(testDish.getId());

        // Then
        verify(dishRepository).deleteById(testDish.getId());
    }

    @Test
    void whenDeleteNonExistentDish_thenThrowException() {
        // Given
        when(dishRepository.existsById(testDish.getId())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> dishService.deleteDish(testDish.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dish not found");
    }

    @Test
    void whenGetDishById_thenReturnDish() {
        // Given
        when(dishRepository.findById(testDish.getId())).thenReturn(Optional.of(testDish));

        // When
        Dish foundDish = dishService.getDishById(testDish.getId());

        // Then
        assertThat(foundDish).isNotNull();
        assertThat(foundDish.getId()).isEqualTo(testDish.getId());
    }

    @Test
    void whenGetNonExistentDishById_thenThrowException() {
        // Given
        when(dishRepository.findById(testDish.getId())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> dishService.getDishById(testDish.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dish not found");
    }

    @Test
    void whenGetAllDishes_thenReturnAllDishes() {
        // Given
        List<Dish> dishes = Arrays.asList(testDish);
        when(dishRepository.findAll()).thenReturn(dishes);

        // When
        List<Dish> foundDishes = dishService.getAllDishes();

        // Then
        assertThat(foundDishes).isNotEmpty();
        assertThat(foundDishes).hasSize(1);
        assertThat(foundDishes.get(0).getName()).isEqualTo(testDish.getName());
    }

    @Test
    void whenSearchDishesByName_thenReturnMatchingDishes() {
        // Given
        List<Dish> dishes = Arrays.asList(testDish);
        when(dishRepository.findByNameContainingIgnoreCase("test")).thenReturn(dishes);

        // When
        List<Dish> foundDishes = dishService.searchDishesByName("test");

        // Then
        assertThat(foundDishes).isNotEmpty();
        assertThat(foundDishes.get(0).getName()).containsIgnoringCase("test");
    }

    @Test
    void whenGetDishesByPriceRange_thenReturnMatchingDishes() {
        // Given
        List<Dish> dishes = Arrays.asList(testDish);
        BigDecimal minPrice = new BigDecimal("5.00");
        BigDecimal maxPrice = new BigDecimal("15.00");
        when(dishRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(minPrice, maxPrice))
                .thenReturn(dishes);

        // When
        List<Dish> foundDishes = dishService.getDishesByPriceRange(minPrice, maxPrice);

        // Then
        assertThat(foundDishes).isNotEmpty();
        assertThat(foundDishes.get(0).getPrice()).isBetween(minPrice, maxPrice);
    }

    @Test
    void whenAddIngredientToDish_thenIngredientIsAdded() {
        // Given
        when(dishRepository.findById(testDish.getId())).thenReturn(Optional.of(testDish));
        when(ingredientRepository.findById(testIngredient.getId())).thenReturn(Optional.of(testIngredient));
        when(dishRepository.save(any(Dish.class))).thenReturn(testDish);

        // When
        Dish updatedDish = dishService.addIngredientToDish(testDish.getId(), testIngredient.getId(), 1.0);

        // Then
        assertThat(updatedDish).isNotNull();
        verify(dishRepository).save(any(Dish.class));
    }

    @Test
    void whenAddNonExistentIngredientToDish_thenThrowException() {
        // Given
        when(dishRepository.findById(testDish.getId())).thenReturn(Optional.of(testDish));
        when(ingredientRepository.findById(testIngredient.getId())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> dishService.addIngredientToDish(testDish.getId(), testIngredient.getId(), 1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ingredient not found");
    }

    @Test
    void whenRemoveIngredientFromDish_thenIngredientIsRemoved() {
        // Given
        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setDish(testDish);
        dishIngredient.setIngredient(testIngredient);
        dishIngredient.setQuantity(1.0);
        testDish.getIngredients().add(dishIngredient);

        when(dishRepository.findById(testDish.getId())).thenReturn(Optional.of(testDish));
        when(dishRepository.save(any(Dish.class))).thenReturn(testDish);

        // When
        Dish updatedDish = dishService.removeIngredientFromDish(testDish.getId(), testIngredient.getId());

        // Then
        assertThat(updatedDish).isNotNull();
        assertThat(updatedDish.getIngredients()).isEmpty();
        verify(dishRepository).save(any(Dish.class));
    }
} 