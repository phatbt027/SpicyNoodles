package com.phastel.SpicyNoodles.service;

import com.phastel.SpicyNoodles.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    User getUserByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> getAllUsers();
} 