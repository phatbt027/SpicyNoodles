package com.phastel.SpicyNoodles.service.impl;

import com.phastel.SpicyNoodles.entity.User;
import com.phastel.SpicyNoodles.repository.UserRepository;
import com.phastel.SpicyNoodles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        User existingUser = getUserById(user.getId());
        if (!existingUser.getUsername().equals(user.getUsername()) && existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (!existingUser.getEmail().equals(user.getEmail()) && existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }
} 