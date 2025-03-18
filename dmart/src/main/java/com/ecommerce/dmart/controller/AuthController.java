package com.ecommerce.dmart.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dmart.config.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	private final AuthenticationManager authManager;
	private final JwtUtil jwtUtil;

	public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
		super();
		this.authManager = authManager;
		this.jwtUtil = jwtUtil;

	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password)
	{
		logger.info("Login attempt for user: {}", username);
		 try {
			  authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	            String token = jwtUtil.generateToken(username);
	            logger.info("User {} logged in successfully", username);
	            return ResponseEntity.ok(token);
		 }catch (Exception e) {
	            logger.error("Login failed for user {}: {}", username, e.getMessage());
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		
	}
	}
}	

