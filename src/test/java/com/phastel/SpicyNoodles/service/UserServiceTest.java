package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.Role;
import com.phastel.SpicyNoodles.entity.User;
import com.phastel.SpicyNoodles.repository.UserRepository;
import com.phastel.SpicyNoodles.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        testUser.setRole(Role.STAFF);
        testUser.setEnabled(true);
    }

    @Test
    void whenCreateUser_thenUserIsCreated() {
        // Given
        when(userRepository.existsByUsername(testUser.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User createdUser = userService.createUser(testUser);

        // Then
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(createdUser.getEmail()).isEqualTo(testUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void whenCreateUserWithExistingUsername_thenThrowException() {
        // Given
        when(userRepository.existsByUsername(testUser.getUsername())).thenReturn(true);

        // When/Then
        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username already exists");
    }

    @Test
    void whenCreateUserWithExistingEmail_thenThrowException() {
        // Given
        when(userRepository.existsByUsername(testUser.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);

        // When/Then
        assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void whenUpdateUser_thenUserIsUpdated() {
        // Given
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User updatedUser = userService.updateUser(testUser);

        // Then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo(testUser.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void whenUpdateUserWithNewPassword_thenPasswordIsEncoded() {
        // Given
        String newPassword = "newPassword";
        testUser.setPassword(newPassword);
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.updateUser(testUser);

        // Then
        verify(passwordEncoder).encode(newPassword);
    }

    @Test
    void whenDeleteUser_thenUserIsDeleted() {
        // Given
        when(userRepository.existsById(testUser.getId())).thenReturn(true);

        // When
        userService.deleteUser(testUser.getId());

        // Then
        verify(userRepository).deleteById(testUser.getId());
    }

    @Test
    void whenDeleteNonExistentUser_thenThrowException() {
        // Given
        when(userRepository.existsById(testUser.getId())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> userService.deleteUser(testUser.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void whenGetUserById_thenReturnUser() {
        // Given
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        // When
        User foundUser = userService.getUserById(testUser.getId());

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(testUser.getId());
    }

    @Test
    void whenGetUserByNonExistentId_thenThrowException() {
        // Given
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> userService.getUserById(testUser.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void whenGetUserByUsername_thenReturnUser() {
        // Given
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        // When
        User foundUser = userService.getUserByUsername(testUser.getUsername());

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(testUser.getUsername());
    }

    @Test
    void whenGetUserByNonExistentUsername_thenThrowException() {
        // Given
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> userService.getUserByUsername(testUser.getUsername()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void whenExistsByUsername_thenReturnCorrectResult() {
        // Given
        when(userRepository.existsByUsername(testUser.getUsername())).thenReturn(true);

        // When
        boolean exists = userService.existsByUsername(testUser.getUsername());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void whenExistsByEmail_thenReturnCorrectResult() {
        // Given
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);

        // When
        boolean exists = userService.existsByEmail(testUser.getEmail());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void whenGetAllUsers_thenReturnAllUsers() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> foundUsers = userService.getAllUsers();

        // Then
        assertThat(foundUsers).isNotEmpty();
        assertThat(foundUsers).hasSize(1);
        assertThat(foundUsers.get(0).getUsername()).isEqualTo(testUser.getUsername());
    }
} 