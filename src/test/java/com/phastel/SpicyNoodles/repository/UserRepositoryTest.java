package com.phastel.SpicyNoodles.repository;

import com.phastel.SpicyNoodles.entity.Role;
import com.phastel.SpicyNoodles.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Create a test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        testUser.setRole(Role.STAFF);
        testUser.setEnabled(true);
    }

    @Test
    void whenSaveUser_thenUserIsSaved() {
        // When
        User savedUser = userRepository.save(testUser);

        // Then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(savedUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(savedUser.getRole()).isEqualTo(testUser.getRole());
        assertThat(savedUser.isEnabled()).isEqualTo(testUser.isEnabled());
    }

    @Test
    void whenFindByUsername_thenReturnUser() {
        // Given
        entityManager.persist(testUser);
        entityManager.flush();

        // When
        Optional<User> found = userRepository.findByUsername(testUser.getUsername());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo(testUser.getUsername());
    }

    @Test
    void whenFindByEmail_thenReturnUser() {
        // Given
        entityManager.persist(testUser);
        entityManager.flush();

        // When
        Optional<User> found = userRepository.findByEmail(testUser.getEmail());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void whenExistsByUsername_thenReturnTrue() {
        // Given
        entityManager.persist(testUser);
        entityManager.flush();

        // When
        boolean exists = userRepository.existsByUsername(testUser.getUsername());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void whenExistsByEmail_thenReturnTrue() {
        // Given
        entityManager.persist(testUser);
        entityManager.flush();

        // When
        boolean exists = userRepository.existsByEmail(testUser.getEmail());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void whenFindByNonExistentUsername_thenReturnEmpty() {
        // When
        Optional<User> found = userRepository.findByUsername("nonexistent");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void whenFindByNonExistentEmail_thenReturnEmpty() {
        // When
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void whenExistsByNonExistentUsername_thenReturnFalse() {
        // When
        boolean exists = userRepository.existsByUsername("nonexistent");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void whenExistsByNonExistentEmail_thenReturnFalse() {
        // When
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void whenDeleteUser_thenUserIsRemoved() {
        // Given
        User savedUser = entityManager.persist(testUser);
        entityManager.flush();

        // When
        userRepository.deleteById(savedUser.getId());
        Optional<User> found = userRepository.findById(savedUser.getId());

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void whenUpdateUser_thenUserIsUpdated() {
        // Given
        User savedUser = entityManager.persist(testUser);
        entityManager.flush();

        // When
        savedUser.setUsername("updateduser");
        savedUser.setEmail("updated@example.com");
        User updatedUser = userRepository.save(savedUser);

        // Then
        assertThat(updatedUser.getUsername()).isEqualTo("updateduser");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
    }
} 