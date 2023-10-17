package com.example.visites.services;

import java.util.List;

import com.example.visites.dto.PasswordRequest;
import com.example.visites.dto.UserRequest;
import com.example.visites.dto.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
	
	ResponseEntity<List<UserResponse>> index();
	
	ResponseEntity<UserResponse> show(Long Id);
	
	ResponseEntity<UserResponse> create(UserRequest user);

	ResponseEntity<UserResponse> update(UserRequest user, Long id);
	
	ResponseEntity<?> delete(Long id);

	ResponseEntity<List<UserResponse>> records(String search);

	ResponseEntity<UserResponse> modifyPassword(Long id, PasswordRequest request);
}
