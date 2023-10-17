package com.example.visites.services;

import java.util.List;

import com.example.visites.dto.AvisRequest;
import com.example.visites.dto.AvisResponse;
import org.springframework.http.ResponseEntity;

public interface AvisService {
	
	ResponseEntity<List<AvisResponse>> index();
	
	ResponseEntity<AvisResponse> show(Long id);
	
	ResponseEntity<AvisResponse> create(AvisRequest avis);

	ResponseEntity<AvisResponse> update(AvisRequest avis, Long id);
	
	ResponseEntity<?> delete(Long id);
	
}
