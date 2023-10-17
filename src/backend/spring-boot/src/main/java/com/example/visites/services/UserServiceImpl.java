package com.example.visites.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.example.visites.configs.AppConstants;
import com.example.visites.dto.PasswordRequest;
import com.example.visites.exceptions.APIException;
import com.example.visites.exceptions.ResourceNotFoundException;
import com.example.visites.models.EmailDetails;
import com.example.visites.models.Profile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.visites.dto.UserRequest;
import com.example.visites.dto.UserResponse;
import com.example.visites.models.Role;
import com.example.visites.models.User;
import com.example.visites.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;

	private final ProfileService profileService;
	private final PasswordEncoder passwordEncoder;

	private final ModelMapper modelMapper;

	private final EmailService emailService;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, ProfileService profileService, PasswordEncoder passwordEncoder,
	                       ModelMapper modelMapper, EmailService emailService) {
		this.userRepository = userRepository;
		this.profileService = profileService;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
		this.emailService = emailService;
	}
	

	@Override
	public ResponseEntity<List<UserResponse>> index() {
		List<UserResponse> users = userRepository.findAll()
				.stream().map(el->modelMapper.map(el, UserResponse.class))
				.collect(Collectors.toList());
		return  new ResponseEntity<>(users, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<UserResponse> show(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("L'User", "d'Id", id));
		return new ResponseEntity<>(modelMapper.map(user, UserResponse.class), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<UserResponse> create(UserRequest user) {
		User newUser = modelMapper.map(user, User.class);
		List<Role> roles = new ArrayList<>();
		for (Role role : newUser.getRoles()) {
			roles.add(role);
		}
		String password;
		if (newUser.getPassword() == null)
			password = ramdomPassword();
		else {
			password = newUser.getPassword();
			if (!(password.matches(AppConstants.PASSWORD_REGEX)))
				throw new APIException("Le mot de passe doit avoir min 8 carateres et contenir au moins : " +
						"Un chiffre , une lettre majuscule, une lettre miniscule, un caractere special, pas d'espace");
		}
		newUser.setPassword(passwordEncoder.encode(password));
		newUser.setRoles(roles);
		UserResponse saved = modelMapper.map(userRepository.save(newUser), UserResponse.class);
		user.setPassword(password);
		sendMailToUser(user);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<UserResponse> update(UserRequest user, Long id) {
		User tmp = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("L'User que vous voulez modifier ", "d'Id", id));
		User oldUser = modelMapper.map(user, User.class);
		oldUser.setId(id);
		oldUser.setProfile(tmp.getProfile());
		oldUser.setPassword(tmp.getPassword());
		UserResponse updated = modelMapper.map(userRepository.save(oldUser), UserResponse.class);
		return new ResponseEntity<>(updated, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<UserResponse> modifyPassword(Long id, PasswordRequest request) {
		User oldUser = userRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("L'User que vous voulez modifier ", "d'Id", id));
		if (passwordEncoder.matches(request.getOldPassword(), oldUser.getPassword())) {
			savePassword(oldUser, request.getNewPassword());
			UserResponse modified = modelMapper.map(userRepository.save(oldUser), UserResponse.class);
			return new ResponseEntity<>(modified, HttpStatus.ACCEPTED);
		} else {
			throw new APIException("Votre ancien mot de passe ne correspond pas au mot de passe que vous avez entre");
		}
	}


	@Override
	public ResponseEntity<?> delete(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("L'User que vous voulez supprimer ", "d'Id", id));
		Profile profile = user.getProfile();
		profileService.delete(profile);
		userRepository.delete(user);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<List<UserResponse>> records(String search) {
		List<User> users = userRepository.findByNomContainingOrPrenomContainingOrUsernameContainingOrEmailContainingOrTelContaining(search, search, search, search, search);
		List<UserResponse> resp = users.stream().map(el->modelMapper.map(el, UserResponse.class))
				.collect(Collectors.toList());
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	public String ramdomPassword(){
		StringBuilder passwordBuilder = new StringBuilder(10);
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			int index = random.nextInt(AppConstants.CHARACTERS_ALLOWED_FOR_PASSWORD.length());
			passwordBuilder.append(AppConstants.CHARACTERS_ALLOWED_FOR_PASSWORD.charAt(index));
		}
		return passwordBuilder.toString();
	}
	public  void savePassword(User user, String password){
		if (!(password.matches(AppConstants.PASSWORD_REGEX)))
			throw new APIException("Le mot de passe doit avoir min 8 carateres et contenir au moins : " +
							"Un chiffre , une lettre majuscule, une lettre minuscule, un caractere special, pas d'espace");
		user.setPassword(passwordEncoder.encode(password));
	}

	public void sendMailToUser(UserRequest user) {
		String message = "Bienvenu M./Mme " + user.getPrenom() + " " + user.getNom() + "\n" +
						"Heureux de vous avoir dans notre entreprise en tant que " + user.getPoste() + "\n" +
						"Voici vos coordonnees pour acceder a notre plateforme:\n" +
						"Username: " + user.getEmail() + "\n" +
						"Mot de passe: " + user.getPassword() + "\n";
		EmailDetails email = new EmailDetails(user.getEmail(), "Accueil", message);
		emailService.sendSimpleMail(email);
	}
}
