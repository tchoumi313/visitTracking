package com.example.visites.services;

import com.example.visites.models.EmailDetails;
import com.example.visites.models.Visite;
import com.example.visites.repositories.VisiteRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {

	@Autowired
	private VisiteRepository visiteRepository;

	@Autowired
	private UserDetailsService userDetailsService;

	private EmailService emailService;

	@Scheduled(cron = "0 0 * * * *")
	public void notifyUsers() {
		List<Visite> visites = visiteRepository.findByDateVisiteAndType(LocalDate.now(), "rendez-vous");
		for (Visite visite : visites) {
			long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), LocalDateTime.of(visite.getDateVisite().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), visite.getHeureDebut()));
			if (hours <= 24) {
				// Envoyer un e-mail au visiteur
				notifyVisitor(visite, hours);

				// Envoyer un e-mail à l'employé
				notifyEmployee(visite, hours);
			}
		}
	}

	private void notifyVisitor(Visite visite, long hours) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(visite.getVisiteur().getEmail());
		String subject = "Rappel - rendez-vous à " + visite.getHeureDebut().toString();
		String content = "Bonjour " + userDetails.getUsername() + ",<br><br>";
		content += "Ceci est un rappel pour votre rendez-vous prévu le " + visite.getDateVisite().toString();
		content += " à " + visite.getHeureDebut().toString() + ".<br><br>";
		content += "Il vous reste " + hours + " heures avant votre rendez-vous.<br><br>";
		content += "Cordialement,\nL'équipe de Visites.";
		EmailDetails mail = new EmailDetails(userDetails.getUsername(), subject, content);
		emailService.sendSimpleMail(mail);
	}

	private void notifyEmployee(Visite visite, long hours) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(visite.getUser().getEmail());
		String subject = "Rappel - rendez-vous à " + visite.getHeureDebut().toString();
		String content = "Bonjour " + userDetails.getUsername() + ",<br><br>";
		content += "Ceci est un rappel pour votre rendez-vous prévu le " + visite.getDateVisite().toString();
		content += " à " + visite.getHeureDebut().toString() + ".<br><br>";
		content += "Il vous reste " + hours + " heures avant votre rendez-vous.<br><br>";
		content += "Cordialement,<br>L'équipe de Visites.";
		EmailDetails mail = new EmailDetails(userDetails.getUsername(), subject, content);
		emailService.sendSimpleMail(mail);
	}

}
