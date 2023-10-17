package com.example.visites.dto;

import java.time.LocalTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisiteRequest {
    private String motif;
    private String heureDebut;
    private String heureFin;
    private Date dateVisite;
    private String type;
    private UserResponse user;
    private VisiteurResponse visiteur;
}
