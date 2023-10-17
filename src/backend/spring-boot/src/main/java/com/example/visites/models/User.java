package com.example.visites.models;

import java.io.Serial;
import java.sql.Date;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends Personne {

    @Serial
    private static final long serialVersionUID = 1L;

	@Column(name = "poste")
    @NotNull(message = "poste : Ce champ est obligatoire")
    @Size(min = 5, max = 50, message = "poste : Le taille de ce champ doit etre comprise entre 5 et 50")
    private String poste;

    @Column(name = "username")
    @NotBlank(message = "username : Ce champ ne doit pas etre vide")
    @NotNull(message = "username : Ce champ est obligatoire")
    private String username;

    /*
    * ^ début de chaîne
    * (?=.*[0-9]) doit contenir au moins un chiffre
    * (?=.*[a-z]) doit contenir au moins une lettre minuscule
    * (?=.*[A-Z]) doit contenir au moins une lettre majuscule
    * (?=.*[@#$%^&+=\\-_!]) doit contenir au moins un caractère spécial
    * (?=\\S+$) ne doit pas contenir d'espaces
    * .{8,} doit avoir au moins 8 caractères
    * $ fin de chaîne
    * @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=\\-_!])(?=\\S+$).{8,}$", message = "Mot de passe faible")
    */
    private String password;

		@OneToOne
		@JoinColumn(name = "profile_id")
		private Profile profile;

	@ManyToOne
    private Bureau bureau;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Visite> visites;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
    	joinColumns = @JoinColumn(name = "user_id"),
    	inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
    

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Long id, String nom, String prenom, String sexe, String email, Date dateNais, String tel, String poste, String username, String password) {
		super(id, nom, prenom, sexe, email, dateNais, tel);
		this.poste = poste;
		this.username = username;
		this.password = password;
	}
	
}