package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Dish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DishRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private DishRepository dishRepository;

    private Dish dish;

    @BeforeEach
    void setUp() {
        dish = new Dish();
        dish.setName("Spicy Noodles");
        dish.setPrice(new BigDecimal("9.99"));
        dish.setDescription("Delicious spicy noodles");
        dish.setAvailable(true);
        dish.setCategory("MAIN_COURSE");
    }

    @Test
    void whenSaveDish_thenDishIsSaved() {
        Dish saved = dishRepository.save(dish);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(dish.getName());
    }

    @Test
    void whenFindByNameContainingIgnoreCase_thenReturnDish() {
        entityManager.persist(dish);
        entityManager.flush();
        List<Dish> found = dishRepository.findByNameContainingIgnoreCase("spicy");
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getName()).containsIgnoringCase("spicy");
    }

    @Test
    void whenFindByPriceLessThanEqual_thenReturnDish() {
        entityManager.persist(dish);
        entityManager.flush();
        List<Dish> found = dishRepository.findByPriceLessThanEqual(new BigDecimal("10.00"));
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getPrice()).isLessThanOrEqualTo(new BigDecimal("10.00"));
    }

    @Test
    void whenFindByPriceGreaterThanEqual_thenReturnDish() {
        entityManager.persist(dish);
        entityManager.flush();
        List<Dish> found = dishRepository.findByPriceGreaterThanEqual(new BigDecimal("9.00"));
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getPrice()).isGreaterThanOrEqualTo(new BigDecimal("9.00"));
    }

    @Test
    void whenFindByPriceGreaterThanEqualAndPriceLessThanEqual_thenReturnDish() {
        entityManager.persist(dish);
        entityManager.flush();
        List<Dish> found = dishRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(new BigDecimal("9.00"), new BigDecimal("10.00"));
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getPrice()).isBetween(new BigDecimal("9.00"), new BigDecimal("10.00"));
    }

    @Test
    void whenDeleteDish_thenDishIsRemoved() {
        Dish saved = entityManager.persist(dish);
        entityManager.flush();
        dishRepository.deleteById(saved.getId());
        assertThat(dishRepository.findById(saved.getId())).isEmpty();
    }
} 