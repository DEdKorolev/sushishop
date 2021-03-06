package com.example.sushishop.service;

import com.example.sushishop.domain.User;
import com.example.sushishop.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService { // security
	boolean save(UserDto userDTO);
	void save(User user);
	List<UserDto> getAll();
	User findByName(String name);
	void updateProfile(UserDto userDTO);
	void delete(Long id);
}
