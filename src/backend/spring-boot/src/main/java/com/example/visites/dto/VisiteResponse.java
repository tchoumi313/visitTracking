package com.example.visites.dto;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisiteResponse {
	private Long id;
    private String motif;
    private String heureDebut;
    private String heureFin;
    private Date dateVisite;
    private String type;
    private String status;
    private UserResponse user;
    private VisiteurResponse visiteur;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
