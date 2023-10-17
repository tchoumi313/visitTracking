package com.example.visites.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BureauResponse{
	private Long id;
	private String batiment;
	private String etage;
	private String porte;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
