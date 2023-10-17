package com.example.visites.services;

import com.example.visites.models.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	@Autowired
	private final JavaMailSender mailSender;

	@Override
	public void sendSimpleMail(EmailDetails emailDetails) {
	  SimpleMailMessage mail = new SimpleMailMessage();
	  mail.setTo(emailDetails.getDestination());
	  mail.setSubject(emailDetails.getSubject());
	  mail.setText(emailDetails.getMessage());

	  mailSender.send(mail);
		System.out.println("mail send successfully to :" + emailDetails.getDestination());
	}

	@Override
	public void sendEmailWithLinks(EmailDetails details) throws MessagingException {
	  MimeMessage message = mailSender.createMimeMessage();
	  MimeMessageHelper helper = new MimeMessageHelper(message, true);

	  helper.setTo(details.getDestination());
	  helper.setSubject(details.getSubject());

		helper.setText("<br><br>");
		helper.setText("<html><body><a href='" + details.getLink() + "'>" + details.getLinkDescription() + "</body></html>", true);

		mailSender.send(message);
	}
}
