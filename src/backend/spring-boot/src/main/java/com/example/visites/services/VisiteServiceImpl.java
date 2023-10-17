package com.example.visites.services;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.example.visites.configs.AppConstants;
import com.example.visites.exceptions.APIException;
import com.example.visites.exceptions.ResourceNotFoundException;
import com.example.visites.models.EmailDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.visites.dto.VisiteRequest;
import com.example.visites.dto.VisiteResponse;
import com.example.visites.models.Visite;
import com.example.visites.repositories.VisiteRepository;

@Service
public class VisiteServiceImpl implements VisiteService {
	
	private final VisiteRepository visiteRepository;
	private final ModelMapper modelMapper;

	private final EmailService emailService;

	@Autowired
	public VisiteServiceImpl(VisiteRepository visiteRepository, ModelMapper modelMapper, EmailService emailService) {
		this.visiteRepository = visiteRepository;
		this.modelMapper = modelMapper;
		this.emailService = emailService;
	}
	@Override
	public ResponseEntity<List<VisiteResponse>> indexVisites() {
		List<VisiteResponse> visites = visiteRepository.findByType(AppConstants.type[0])
				.stream().map(el->modelMapper.map(el, VisiteResponse.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(visites, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<VisiteResponse>> indexRDV() {
		List<VisiteResponse> visites = visiteRepository.findByType(AppConstants.type[1])
						.stream().map(el->modelMapper.map(el, VisiteResponse.class))
						.collect(Collectors.toList());
		for (VisiteResponse visite: visites) {
			visite.setStatus(setStatus(visite));
		}
		return new ResponseEntity<>(visites, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<VisiteResponse> show(Long id) {
		Visite visite = visiteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("La Visite", "d'Id", id));
		return new ResponseEntity<>(modelMapper.map(visite, VisiteResponse.class), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<VisiteResponse> create(VisiteRequest visite) {
		LocalTime debut = extractTime(visite.getHeureDebut());
		LocalTime fin = extractTime(visite.getHeureFin());
		Visite newVisite = modelMapper.map(visite, Visite.class);
		if (fin.isBefore(debut))
			throw new APIException("L'heure de fin doit etre apres a l'heure de debut");
		newVisite.setHeureDebut(debut);
		newVisite.setHeureFin(fin);
		VisiteResponse saved = modelMapper.map(visiteRepository.save(newVisite), VisiteResponse.class);
		if (saved.getType().equals(AppConstants.type[0]))
			sendMailForVisite(saved);
		else
			sendMailForRDV(saved);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<VisiteResponse> update(VisiteRequest visite, Long id) {
		String oldType = visite.getType();
		LocalTime debut = extractTime(visite.getHeureDebut());
		LocalTime fin = extractTime(visite.getHeureFin());
		visiteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("La Visite que vous voulez modifier ", "d'Id", id));
		Visite oldVisite = modelMapper.map(visite, Visite.class);
		oldVisite.setId(id);
		if (oldVisite.getDateVisite().after(new Date())) {
			oldVisite.setType(AppConstants.type[1]);
			System.out.println("--- Rendez-vous ---");
		}
		if (fin.isBefore(debut))
			throw new APIException("L'heure de fin doit etre apres a l'heure de debut");
		oldVisite.setHeureDebut(debut);
		oldVisite.setHeureFin(fin);
		VisiteResponse updated = modelMapper.map(visiteRepository.save(oldVisite), VisiteResponse.class);
		if (oldType.equals(updated.getType()))
			sendMailVisitToRDV(updated);
		return new ResponseEntity<>(updated, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		Visite visite = visiteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("La Visite que vous voulez supprimer ", "d'Id", id));
		visiteRepository.delete(visite);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<List<VisiteResponse>> records(String search) {
		List<Visite> visites = visiteRepository.findByMotifContaining(search);
		List<VisiteResponse> resp = visites.stream().map(el->modelMapper.map(el, VisiteResponse.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<VisiteResponse>> sort(String motif) {
		List<Visite> visites = visiteRepository.findByMotifContaining(motif);
		visites.removeIf(v -> v.getAvis() != null);
		List<VisiteResponse> resp = visites.stream().map(el->modelMapper.map(el, VisiteResponse.class))
				.toList();
		return  new ResponseEntity<>(resp, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<VisiteResponse>> getVisiteByEmployeId(Long employeId) {
		List<Visite> visites = visiteRepository.findByUserIdOrType(employeId, AppConstants.type[0]);
		List<VisiteResponse> resp = visites.stream()
						.map(el -> modelMapper.map(el, VisiteResponse.class)).toList();
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<VisiteResponse> createOrdinary(VisiteRequest visite) {
		LocalTime fin = extractTime(visite.getHeureFin());
		Visite newVisite = modelMapper.map(visite, Visite.class);
		newVisite.setDateVisite(new Date());
		newVisite.setHeureDebut(LocalTime.now());
		newVisite.setType("ordinaire");
		if (fin.isBefore(newVisite.getHeureDebut()))
			throw new APIException("L'heure de fin doit etre apres a l'heure de debut");
		newVisite.setHeureFin(fin);
		VisiteResponse saved = modelMapper.map(visiteRepository.save(newVisite), VisiteResponse.class);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}
	private LocalTime extractTime(String date) {
		String[] data = date.split(":");
		return LocalTime.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
	}

	private String setStatus(VisiteResponse visite){
		LocalTime now = LocalTime.now();
		LocalTime debut = extractTime(visite.getHeureDebut());
		LocalTime fin = extractTime(visite.getHeureFin());
		if (visite.getDateVisite().equals(new Date())) {
			if (debut.isBefore(now) && fin.isAfter(now))
				return AppConstants.status[1];
		}
		if (visite.getDateVisite().before(new Date()))
			return AppConstants.status[2];
		if (visite.getDateVisite().after(new Date()))
			return AppConstants.status[0];
		if (debut.isAfter(now))
			return AppConstants.status[0];
		return AppConstants.status[2];
	}

	public void sendMailForVisite(VisiteResponse visite) {
		String userMessage = "Bonjour M./Mme " + visite.getUser().getPrenom() + " " + visite.getUser().getNom() + "\n" +
						"Vous avez recu une visite de M./Mme " + visite.getVisiteur().getPrenom() + " " + visite.getVisiteur().getNom() + ". " +
						"Actuellement il attend dans les locaux.\n" +
						"Si vous ete disponible Veillez signaler ici:\n" +
						"Lien: \n";
		EmailDetails userEmail = new EmailDetails(visite.getUser().getEmail(), "Confirmation de visite", userMessage);
		emailService.sendSimpleMail(userEmail);
		String visitorMessage = "Bonjour M./Mme " + visite.getVisiteur().getPrenom() + " " + visite.getVisiteur().getNom() + "\n" +
						"Vous allez rencontrer notre employe M./Mme " + visite.getVisiteur().getPrenom() + " " + visite.getVisiteur().getNom() + ". " +
						"Veillez vous diriger dans les locaux de son bureau:\n" +
						"Batiment: " + visite.getUser().getBureau().getBatiment() + "\n" +
						"Etage: " + visite.getUser().getBureau().getBatiment() + "\n" +
						"Porte: " + visite.getUser().getBureau().getBatiment() + "\n";
		EmailDetails visitorEmail = new EmailDetails(visite.getVisiteur().getEmail(), "Confirmation de visite", visitorMessage);
		emailService.sendSimpleMail(visitorEmail);
	}

	public void sendMailForRDV(VisiteResponse rdv) {
		String userMessage = "Bonjour M./Mme " + rdv.getUser().getPrenom() + " " + rdv.getUser().getNom() + "\n" +
						"Vous venez de programmer un rendez-vous avec M./Mme " + rdv.getVisiteur().getPrenom() + " " + rdv.getVisiteur().getNom() + ". " +
						"Ce rendez-vous a ete fixe pour: " + rdv.getDateVisite() +
						", entre les  heures " + rdv.getHeureDebut() + "et " + rdv.getHeureFin() + "\n" +
						"Si vous ne serez pas disponible Veillez signaler ici:\n" +
						"Lien: \n";
		EmailDetails userEmail = new EmailDetails(rdv.getUser().getEmail(), "Confirmation de rendez-vous", userMessage);
		emailService.sendSimpleMail(userEmail);
		String visitorMessage = "Bonjour M./Mme " + rdv.getVisiteur().getPrenom() + " " + rdv.getVisiteur().getNom() + "\n" +
						"Vous avez ete programmes pour un rendez-vous avec notre employe M./Mme " + rdv.getVisiteur().getPrenom() + " " + rdv.getVisiteur().getNom() + ". " +
						"Voici les informations sur son bureau:\n" +
						"Batiment: " + rdv.getUser().getBureau().getBatiment() + "\n" +
						"Etage: " + rdv.getUser().getBureau().getBatiment() + "\n" +
						"Porte: " + rdv.getUser().getBureau().getBatiment() + "\n";
		EmailDetails visitorEmail = new EmailDetails(rdv.getVisiteur().getEmail(), "Confirmation de rendez-vous", visitorMessage);
		emailService.sendSimpleMail(visitorEmail);
	}

	public void sendMailVisitToRDV(VisiteResponse rdv) {
		String userMessage = "M./Mme " + rdv.getUser().getPrenom() + " " + rdv.getUser().getNom() + "\n" +
						"Vous venez de repousser la date de visite de M./Mme " + rdv.getVisiteur().getPrenom() + " " + rdv.getVisiteur().getNom() + ". " +
						"Ce rendez-vous a ete fixe pour: " + rdv.getDateVisite() +
						", entre les  heures " + rdv.getHeureDebut() + "et " + rdv.getHeureFin() + "\n";
		EmailDetails userEmail = new EmailDetails(rdv.getUser().getEmail(), "Progammtion de rendez-vous", userMessage);
		emailService.sendSimpleMail(userEmail);
		String visitorMessage = "M./Mme " + rdv.getVisiteur().getPrenom() + " " + rdv.getVisiteur().getNom() + "\n" +
						"Votre visite: " + rdv.getMotif() + " avec notre employe M./Mme " + rdv.getVisiteur().getPrenom() + " " + rdv.getVisiteur().getNom() +
						"viens d'etre repousse a une date ulterieure :\n" +
						"Voici la nouvelle date: " + rdv.getDateVisite() + "\n" +
						"Entre les heures : " + rdv.getHeureDebut() + " et " + rdv.getHeureFin() + "\n";
		EmailDetails visitorEmail = new EmailDetails(rdv.getVisiteur().getEmail(), "Programmation de rendez-vous", visitorMessage);
		emailService.sendSimpleMail(visitorEmail);
	}
}
