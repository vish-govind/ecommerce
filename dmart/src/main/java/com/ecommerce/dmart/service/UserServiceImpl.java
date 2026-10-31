package com.ecommerce.dmart.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.dmart.exception.UserAlreadyExistsException;
import com.ecommerce.dmart.exception.EntityNotFoundException;
import com.ecommerce.dmart.model.User;
import com.ecommerce.dmart.model.Role;
import com.ecommerce.dmart.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public User registerUser(User user) {

        logger.info("Registering user with email: {}", user.getEmail());

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with email " + user.getEmail() + " already exists.");
        }

        // Always assign CUSTOMER role during public registration
        user.setRole(Role.CUSTOMER);

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        logger.info("User with email {} registered successfully", user.getEmail());

        return savedUser;
    }

    // Get user by username
    @Override
    @Cacheable(value = "users", key = "#username")
    public User getUserByUsername(String username) {

        logger.info("Fetching user by username: {}", username);

        return userRepository.findByUserName(username)
                .orElseThrow(() -> {
                    logger.error("User with username '{}' not found", username);
                    return new EntityNotFoundException(
                            "User with username '" + username + "' not found");
                });
    }

    // Update user profile
    @Override
    @CachePut(value = "users", key = "#username")
    public User updateUser(String username, User user) {

        logger.info("Updating user: {}", username);

        User existingUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with username '" + username + "' not found"));

        // Update only permitted fields
        existingUser.setEmail(user.getEmail());

        User updatedUser = userRepository.save(existingUser);

        logger.info("User '{}' updated successfully", username);

        return updatedUser;
    }

    // Delete user
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long userId) {

        logger.info("Deleting user with ID: {}", userId);

        userRepository.deleteById(userId);

        logger.info("User with ID {} deleted successfully", userId);
    }
}
