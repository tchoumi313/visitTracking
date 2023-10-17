package com.example.visites.models;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "bureaux")
@AllArgsConstructor
@Data
public class Bureau implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "batiment")
	@NotBlank(message = "batiment : Ce champ ne doit pas etre vide")
	@NotNull(message = "batiment : Ce champ est obligatoire")
	private String batiment;

	@Column(name = "etage")
	@NotBlank(message = "etage : Ce champ ne doit pas etre vide")
	@NotNull(message = "etage : Ce champ est obligatoire")
	private String etage;
	
	@Column(name = "porte")
	@NotBlank(message = "porte : Ce champ ne doit pas etre vide")
	@NotNull(message = "porte : Ce champ est obligatoire")
	private String porte;
    
    @OneToMany(mappedBy = "bureau", cascade = CascadeType.ALL)
    private List<User> users;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = true)
    private Timestamp updatedAt;

	
	public Bureau() {
		// TODO Auto-generated constructor stub
	}

	public Bureau(Long id, String batiment, String etage, String porte) {
		super();
		this.id = id;
		this.batiment = batiment;
		this.etage = etage;
		this.porte = porte;
	}
	
}