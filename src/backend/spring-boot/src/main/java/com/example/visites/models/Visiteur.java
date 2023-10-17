package com.example.visites.models;

import java.io.Serial;
import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "visiteurs")
@Setter
@Getter
public class Visiteur extends Personne {

    @Serial
	private static final long serialVersionUID = 1L;

	@Column(name = "profession")
	@NotNull(message = "profession : Ce champ est obligatoire")
	@Size(min = 5, max = 50, message = "profession : La taille de ce champ doit etre comprise entre 5 et 50")
    private String profession;

    @OneToMany(mappedBy = "visiteur", cascade = CascadeType.ALL)
    private List<Visite> visites;

	public Visiteur() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Visiteur(Long id, String nom, String prenom, String sexe, String email, Date dateNais, String tel, String profession) {
		super(id, nom, prenom, sexe, email, dateNais, tel);
		this.profession = profession;
	}

}