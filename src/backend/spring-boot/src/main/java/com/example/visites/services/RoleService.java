package com.example.visites.services;

import java.util.List;

import com.example.visites.dto.RoleRequest;
import com.example.visites.dto.RoleResponse;
import org.springframework.http.ResponseEntity;

public interface RoleService {

	
	ResponseEntity<List<RoleResponse>> index();
	
	ResponseEntity<RoleResponse> show(Long Id);
	
	ResponseEntity<RoleResponse> create(RoleRequest role);

	ResponseEntity<RoleResponse> update(RoleRequest role, Long id);
	
	ResponseEntity<?> delete(Long id);

	ResponseEntity<List<RoleResponse>> records(String name);
}
