package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.SoftDrink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SoftDrinkRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private SoftDrinkRepository softDrinkRepository;

    private SoftDrink softDrink;

    @BeforeEach
    void setUp() {
        softDrink = new SoftDrink();
        softDrink.setName("Cola");
        softDrink.setPrice(new BigDecimal("2.50"));
        softDrink.setAvailable(true);
        softDrink.setSize("330ml");
        softDrink.setHot(false);
    }

    @Test
    void whenSaveSoftDrink_thenSoftDrinkIsSaved() {
        SoftDrink saved = softDrinkRepository.save(softDrink);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(softDrink.getName());
    }

    @Test
    void whenFindByNameContainingIgnoreCase_thenReturnSoftDrink() {
        entityManager.persist(softDrink);
        entityManager.flush();
        List<SoftDrink> found = softDrinkRepository.findByNameContainingIgnoreCase("cola");
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getName()).containsIgnoringCase("cola");
    }

    @Test
    void whenFindByPriceLessThanEqual_thenReturnSoftDrink() {
        entityManager.persist(softDrink);
        entityManager.flush();
        List<SoftDrink> found = softDrinkRepository.findByPriceLessThanEqual(new BigDecimal("3.00"));
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getPrice()).isLessThanOrEqualTo(new BigDecimal("3.00"));
    }

    @Test
    void whenFindByPriceGreaterThanEqual_thenReturnSoftDrink() {
        entityManager.persist(softDrink);
        entityManager.flush();
        List<SoftDrink> found = softDrinkRepository.findByPriceGreaterThanEqual(new BigDecimal("2.00"));
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getPrice()).isGreaterThanOrEqualTo(new BigDecimal("2.00"));
    }

    @Test
    void whenFindByPriceGreaterThanEqualAndPriceLessThanEqual_thenReturnSoftDrink() {
        entityManager.persist(softDrink);
        entityManager.flush();
        List<SoftDrink> found = softDrinkRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(new BigDecimal("2.00"), new BigDecimal("3.00"));
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getPrice()).isBetween(new BigDecimal("2.00"), new BigDecimal("3.00"));
    }

    @Test
    void whenDeleteSoftDrink_thenSoftDrinkIsRemoved() {
        SoftDrink saved = entityManager.persist(softDrink);
        entityManager.flush();
        softDrinkRepository.deleteById(saved.getId());
        assertThat(softDrinkRepository.findById(saved.getId())).isEmpty();
    }
} 