package com.example.visites.controllers;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.visites.dto.RoleRequest;
import com.example.visites.dto.RoleResponse;
import com.example.visites.services.RoleService;
import com.example.visites.services.RoleServiceImpl;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {

	private final RoleService roleService;

	@Autowired
	public RoleController(RoleServiceImpl roleService) {
		this.roleService = roleService;
	}
	
	@GetMapping("/")
	public ResponseEntity<List<RoleResponse>> index(){
		return roleService.index();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RoleResponse> show(@PathVariable Long id) {
		return roleService.show(id);
	}

	@PostMapping("/")
	public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest role) {
		return roleService.create(role);
	}

	@PutMapping("/{id}")
	public ResponseEntity<RoleResponse> update(@PathVariable Long id, @Valid @RequestBody RoleRequest role) {
		return roleService.update(role, id);
	}
	

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return roleService.delete(id);
	}

	@GetMapping("/records/{nom}")
	public ResponseEntity<List<RoleResponse>> records(@PathVariable String nom){
		nom = nom.toUpperCase();
		return roleService.records(nom);
	}

}