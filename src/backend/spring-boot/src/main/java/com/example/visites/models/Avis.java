package com.example.visites.models;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "avis")
@AllArgsConstructor
@Data
public class Avis implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "libelle")
	@NotNull(message = "Le champ libelle est obligatoire")
	@Size(min = 5, max = 100, message = "libelle : La taille de ce champ doit etre comprise entre 10 et 100 caracteres !!!")
	private String libelle;
	
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = true)
    private Timestamp updatedAt;

    @OneToOne
    @JoinColumn(name = "visite_id")
    private Visite visite;	
	
    
    public Avis() {
		// TODO Auto-generated constructor stub
	}

	public Avis(Long id, String libelle) {
		super();
		this.id = id;
		this.libelle = libelle;
	}
	
}