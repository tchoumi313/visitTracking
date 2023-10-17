package com.example.visites.services;

import java.util.List;
import java.util.stream.Collectors;

import com.example.visites.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.visites.dto.RoleRequest;
import com.example.visites.dto.RoleResponse;
import com.example.visites.models.Role;
import com.example.visites.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;
	private final ModelMapper modelMapper;
	
	public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public ResponseEntity<List<RoleResponse>> index() {
		List<RoleResponse> roles = roleRepository.findAll()
				.stream().map(el->modelMapper.map(el, RoleResponse.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RoleResponse> show(Long id) {
		Role role = roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Le Role", "d'Id", id));
		return new ResponseEntity<>(modelMapper.map(role, RoleResponse.class),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RoleResponse> create(RoleRequest role) {
		Role newRole = modelMapper.map(role, Role.class);
		RoleResponse saved = modelMapper.map(roleRepository.save(newRole), RoleResponse.class);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<RoleResponse> update(RoleRequest role, Long id) {
		roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Le Role que vous voulez modifier", "d'Id", id));
		Role oldRole = modelMapper.map(role, Role.class);
		oldRole.setId(id);
		RoleResponse updated = modelMapper.map(roleRepository.save(oldRole), RoleResponse.class);
		return new ResponseEntity<>(updated, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		Role role = roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Le Role que voulez supprimer ", "d'Id", id));
		roleRepository.delete(role);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<List<RoleResponse>> records(String name) {
		List<Role> roles = roleRepository.findByNomContaining(name);
		List<RoleResponse> resp = roles.stream().map(el->modelMapper.map(el, RoleResponse.class))
				.toList();
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}


}
