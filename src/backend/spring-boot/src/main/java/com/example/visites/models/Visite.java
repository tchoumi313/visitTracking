package com.example.visites.models;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "visites")
@Data
public class Visite implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "motif")
    @NotNull(message = "motif : Ce champ est obligatoire")
    @Size(min = 5, max = 100, message = "motif : Ce champ doit avoir une taille comprise entre 5 et 100")
    private String motif;

    @Column(name = "heure_debut")
//    @NotBlank(message = "heureDebut : Ce champ ne doit pas etre vide")
    @NotNull(message = "heureDebut : Ce champ est obligatoire")
    private LocalTime heureDebut;

    @Column(name = "heure_fin")
//    @NotBlank(message = "heureFin : Ce champ ne doit pas etre vide")
    @NotNull(message = "heureFin : Ce champ est obligatoire")
    private LocalTime heureFin;

    @Column(name = "date_visite")
//    @NotBlank(message = "dateVisite : Ce champ ne doit pas etre vide")
    @NotNull(message = "dateVisite : Ce champ est obligatoire")
    private Date dateVisite;

    @Column(name = "type")
    @NotNull(message = "type : Ce champ est obligatoire")
    @Pattern(regexp = "^(ordinaire|rendez-vous)$", message = "Le type de visite doit être 'ordinaire' ou 'rendez-vous'")
    private String type;
    
    @OneToOne(mappedBy = "visite", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    private Avis avis;
    
    @ManyToOne
    private User user;

    @ManyToOne
    private Visiteur visiteur;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = true)
    private Timestamp updatedAt;
    
    
	public Visite() {
		// TODO Auto-generated constructor stub
	}

	public Visite(Long id, String motif, LocalTime heureDebut, LocalTime heureFin, Date dateVisite,
			String type) {
		super();
		this.id = id;
		this.motif = motif;
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
		this.dateVisite = dateVisite;
		this.type = type;
	}
	
}