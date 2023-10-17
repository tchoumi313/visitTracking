package com.example.visites.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BureauRequest {
	private String batiment;
	private String etage;
	private String porte;
}
