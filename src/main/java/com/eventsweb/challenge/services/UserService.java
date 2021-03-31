package com.eventsweb.challenge.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eventsweb.challenge.entitites.User;
import com.eventsweb.challenge.repository.UserRepository;
import com.eventsweb.challenge.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {
	
	private UserRepository repository;

	public User findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found! "));
		return entity;
	}
}
