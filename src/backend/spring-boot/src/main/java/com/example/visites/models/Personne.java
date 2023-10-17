package com.example.visites.models;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;

@MappedSuperclass
@AllArgsConstructor
@Data
public abstract class Personne implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    @NotNull(message = "nom : Ce champ est obligatoire")
    @Size(max = 50, message = "nom : La taille max de ce champ doit etre 50")
    private String nom;

    @Column(name = "prenom")
    @NotNull(message = "prenom : Ce champ est obligatoire")
    @Size(max = 50, message = "prenom : La taille max de ce champ doit etre 50")
    private String prenom;

    @Column(name = "sexe")
    @NotNull(message = "sexe : Ce champ est obligatoire")
    @Pattern(regexp = "^(masculin|féminin)$", message = "Le sexe doit être 'masculin' ou 'féminin'")
    private String sexe;

    @Column(name = "email", unique = true)
    @NotNull(message = "email : Ce champ est obligatoire")
    @Email(message = "email invalide")
    private String email;

    @Column(name = "date_nais")
    @NotNull(message = "dateNais : Ce champ est obligatoire")
    private Date dateNais;

    @Column(name = "tel")
    @NotNull(message = "tel : Ce champ est obligatoire")
    @Pattern(regexp = "^(\\+237|237)?[2368]\\d{8}$", message = "Numéro de téléphone invalide")
    private String tel;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = true)
    private Timestamp updatedAt;

    
	public Personne() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Personne(Long id, String nom, String prenom, String sexe, String email, Date dateNais, String tel) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.sexe = sexe;
		this.email = email;
		this.dateNais = dateNais;
		this.tel = tel;
	}

}