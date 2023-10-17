package com.example.visites.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.visites.exceptions.APIException;
import com.example.visites.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.visites.dto.BureauRequest;
import com.example.visites.dto.BureauResponse;
import com.example.visites.models.Bureau;
import com.example.visites.repositories.BureauRepository;

@Service
public class BureauServiceImpl implements BureauService {
    
	private final BureauRepository bureauRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public BureauServiceImpl(BureauRepository bureauRepository, ModelMapper modelMapper) {
		this.bureauRepository = bureauRepository;
		this.modelMapper = modelMapper;
	}
    
	@Override
    public ResponseEntity<List<BureauResponse>> index() {
		List<BureauResponse> bureaux = bureauRepository.findAll()
		.stream().map(el->modelMapper.map(el, BureauResponse.class))
		.collect(Collectors.toList());
    	return new ResponseEntity<>(bureaux, HttpStatus.OK);
    }

	@Override
    public ResponseEntity<BureauResponse> show(Long id) {
    	Bureau bureau = bureauRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Le Bureau", "d'Id", id));
		return new ResponseEntity<>(modelMapper.map(bureau, BureauResponse.class), HttpStatus.OK);
    }
	
	@Override
    public ResponseEntity<BureauResponse> create(BureauRequest bureau) {
    	Bureau newBureau = modelMapper.map(bureau, Bureau.class);
    	Bureau saved = bureauRepository.save(newBureau);
    	return new ResponseEntity<>(modelMapper.map(saved, BureauResponse.class), HttpStatus.CREATED);
    }

	@Override
    public ResponseEntity<BureauResponse> update(BureauRequest bureau, Long id) {
    	bureauRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Le Bureau que vous voulez modifier", "d'Id", id));
		Bureau oldBureau = modelMapper.map(bureau, Bureau.class);
   		oldBureau.setId(id);
   		BureauResponse updated = modelMapper.map(bureauRepository.save(oldBureau), BureauResponse.class);
   		return new ResponseEntity<>(updated, HttpStatus.ACCEPTED);
    }

	@Override
    public ResponseEntity<?> delete(Long id) {
		Bureau optBureau = bureauRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Le Bureau que vous voulez supprimer ", "d'Id", id));
    	bureauRepository.delete(optBureau);
    	return ResponseEntity.noContent().build();
    }

	@Override
	public ResponseEntity<List<BureauResponse>> records(String batiment, String etage, String porte) {
		List<Bureau> bureaux = new ArrayList<>();
		if (batiment != null && etage != null && porte != null) {
			bureaux = bureauRepository.findByBatimentContainingAndEtageContainingAndPorteContaining(batiment, porte, etage);
		} else if (batiment != null && etage != null) {
			bureaux = bureauRepository.findByBatimentContainingAndEtageContaining(batiment, etage);
		}else if (batiment != null && porte != null) {
			bureaux = bureauRepository.findByBatimentContainingAndPorteContaining(batiment, porte);
		}else if (etage != null && porte != null) {
			bureaux = bureauRepository.findByEtageContainingAndPorteContaining(etage, porte);
		}else if (batiment != null) {
			bureaux = bureauRepository.findByBatimentContaining(batiment);
		}else if (etage != null) {
			bureaux = bureauRepository.findByEtageContaining(etage);
		}else if (porte != null) {
			bureaux = bureauRepository.findByPorteContaining(porte);
		}else
			throw new APIException("Le bureau que vous cherchez n'existe pas !!!");
		List<BureauResponse> resp = bureaux.stream().map(el->modelMapper.map(el, BureauResponse.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

}
