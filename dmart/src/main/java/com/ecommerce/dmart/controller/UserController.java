package com.ecommerce.dmart.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dmart.exception.UserAlreadyExistsException;
import com.ecommerce.dmart.model.User;
import com.ecommerce.dmart.service.UserServiceImpl;

@RestController
@RequestMapping("/users")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private final UserServiceImpl userService;

	public UserController(UserServiceImpl userService) {
		super();
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		logger.info("Received request to register user: {}", user.getUserName());
		try {
			userService.registerUser(user);
			logger.info("User registered successfully: {}", user.getUserName());
			return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
		} catch (UserAlreadyExistsException e) {
			logger.error("User registration failed: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}

	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfile(@RequestParam String username) {
		logger.info("Fetching profile for user: {}", username);
		User user = userService.getUserByUsername(username);
		logger.info("Profile fetched successfully for user: {}", username);
		return ResponseEntity.ok(user);
	}

	@PutMapping("/update")
	public ResponseEntity<User> updateUserProfile(@RequestBody User user) {
		logger.info("Updating profile for user: {}", user.getUserName());
		User updatedUser = userService.updateUser(user);
		logger.info("Profile updated successfully for user: {}", user.getUserName());
		return ResponseEntity.ok(updatedUser);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
		logger.warn("Admin attempting to delete user with ID: {}", userId);
		userService.deleteUser(userId);
		logger.info("User deleted successfully with ID: {}", userId);
		return ResponseEntity.ok("User deleted successfully");
	}
}
