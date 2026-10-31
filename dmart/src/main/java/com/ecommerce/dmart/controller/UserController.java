package com.ecommerce.dmart.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.dmart.exception.UserAlreadyExistsException;
import com.ecommerce.dmart.model.User;
import com.ecommerce.dmart.service.UserServiceImpl;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        logger.info("Received registration request");

        try {
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully.");

        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(
            Authentication authentication) {

        String username = authentication.getName();

        User user = userService.getUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUserProfile(
            @RequestBody User user,
            Authentication authentication) {

        String username = authentication.getName();

        User updatedUser = userService.updateUser(username, user);

        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long userId) {

        userService.deleteUser(userId);

        return ResponseEntity.ok("User deleted successfully");
    }
}
