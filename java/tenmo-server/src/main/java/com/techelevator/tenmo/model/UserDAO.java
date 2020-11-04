package com.techelevator.tenmo.model;

import java.util.List;

public interface UserDAO {
	
	public List<User> getAllUsernames();
	
	public User getUserById(Long id);
	
	public User getUserByUsername(String username);
}
