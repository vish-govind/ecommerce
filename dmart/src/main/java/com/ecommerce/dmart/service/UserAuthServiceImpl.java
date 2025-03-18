package com.ecommerce.dmart.service;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.ecommerce.dmart.exception.EntityNotFoundException;
import com.ecommerce.dmart.model.User;
import com.ecommerce.dmart.repository.UserRepository;

@Service
public class UserAuthServiceImpl implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);
	private final UserRepository userRepo;

	public UserAuthServiceImpl(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@Override
	@Cacheable(value = "users", key = "#username")
	public UserDetails loadUserByUsername(String username) {
		logger.info("Loading user by username: {}", username);
		User user = userRepo.findByUserName(username)
							.orElseThrow(() -> {
			logger.error("User with username '{}' not found", username);
			return new EntityNotFoundException("User with username '" + username + "' not found");
		});
		logger.info("User '{}' found, returning UserDetails", username);
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
	}

	@CacheEvict(value = "users", key = "#user.userName")
	public void updateUser(User user) {
		logger.info("Updating user: {}", user.getUserName());
		userRepo.save(user);
		logger.info("User '{}' updated successfully", user.getUserName());
	}

}
