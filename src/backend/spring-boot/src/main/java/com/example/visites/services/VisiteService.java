package com.example.visites.services;

import java.util.List;

import com.example.visites.dto.VisiteRequest;
import com.example.visites.dto.VisiteResponse;
import org.springframework.http.ResponseEntity;

public interface VisiteService {

	ResponseEntity<List<VisiteResponse>> indexVisites();

	ResponseEntity<List<VisiteResponse>> indexRDV();

	ResponseEntity<VisiteResponse> show(Long id);
	
	ResponseEntity<VisiteResponse> create(VisiteRequest visite);

	ResponseEntity<VisiteResponse> update(VisiteRequest visite, Long id);
	
	ResponseEntity<?> delete(Long id);

    ResponseEntity<List<VisiteResponse>> records(String search);

    ResponseEntity<List<VisiteResponse>> sort(String motif);

	ResponseEntity<List<VisiteResponse>> getVisiteByEmployeId(Long employeId);

	ResponseEntity<VisiteResponse> createOrdinary(VisiteRequest visite);
}
