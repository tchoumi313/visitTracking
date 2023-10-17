package com.example.visites.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.visites.dto.BureauRequest;
import com.example.visites.dto.BureauResponse;

public interface BureauService {
	
	ResponseEntity<List<BureauResponse>> index();
	
	ResponseEntity<BureauResponse> show(Long Id);
	
	ResponseEntity<BureauResponse> create(BureauRequest bureau);

	ResponseEntity<BureauResponse> update(BureauRequest bureau, Long id);
	
	ResponseEntity<?> delete(Long id);

	ResponseEntity<List<BureauResponse>> records(String batiment, String etage, String porte);
}
