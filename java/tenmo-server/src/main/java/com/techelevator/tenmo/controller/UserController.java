package com.techelevator.tenmo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.model.JDBCUserDAO;
import com.techelevator.tenmo.model.User;

@RestController
@RequestMapping(value = "users")
public class UserController {
	
	private JDBCUserDAO userDao;
	
	public UserController(JDBCUserDAO dao) {
		this.userDao = dao;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		List<User> users = userDao.getAllUsers();
		return users;
	}
}
