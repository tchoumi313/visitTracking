package com.example.visites.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
	private Long id;
	private String nomImg;
	private Timestamp createdAt;
	private Timestamp updatedAt;
}
