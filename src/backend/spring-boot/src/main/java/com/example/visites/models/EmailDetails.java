package com.example.visites.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {

  private String destination;
  private String subject;
  private String message;
  private String link;
  private String linkDescription;
  private String attachment;

  public EmailDetails(String email, String subject, String message) {
    this.destination = email;
    this.subject = subject;
    this.message = message;
  }

  public EmailDetails(String email, String subject, String message, String link, String linkDescription) {
    this.destination = email;
    this.subject = subject;
    this.message = message;
    this.link = link;
    this.linkDescription = linkDescription;
  }
}
