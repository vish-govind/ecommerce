package com.ecommerce.dmart.service;

import com.ecommerce.dmart.model.User;

public interface UserService {
	
	User registerUser(User user);
	User getUserByUsername(String username);
	User updateUser(String username, User user);
	void deleteUser(Long userId);

}
