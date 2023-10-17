package com.example.visites.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
	private Long id;
	private String nom;
	private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
