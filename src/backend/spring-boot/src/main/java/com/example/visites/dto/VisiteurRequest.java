package com.example.visites.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisiteurRequest {
    private String nom;
    private String prenom;
    private String sexe;
    private String email;
    private Date dateNais;
    private String tel;
    private String profession;
}