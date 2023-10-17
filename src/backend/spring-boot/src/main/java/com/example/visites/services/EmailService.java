package com.example.visites.services;

import com.example.visites.models.EmailDetails;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {

  @Async
  public void sendSimpleMail(EmailDetails details);

  void sendEmailWithLinks(EmailDetails details) throws MessagingException;
}
