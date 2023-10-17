package com.example.visites.controllers;

import java.util.List;

import com.example.visites.dto.PasswordRequest;
import com.example.visites.dto.ProfileResponse;
import com.example.visites.services.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.visites.dto.UserRequest;
import com.example.visites.dto.UserResponse;
import com.example.visites.services.UserService;
import com.example.visites.services.UserServiceImpl;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final ProfileService profileService;

	@Autowired
	public UserController(UserServiceImpl userService, ProfileService profileService) {
		this.userService = userService;
		this.profileService = profileService;
	}
	
	@GetMapping("/")
	public ResponseEntity<List<UserResponse>> index(){
		return userService.index();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> show(@PathVariable Long id) {
		return userService.show(id);
	}

	@PostMapping("/")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest user) {
		//Attribution de l'image par defaut
		user.setProfile(profileService.setDefaultProfile(user.getEmail()).getBody());
		return userService.create(user);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest user) {
		return userService.update(user, id);
	}

	@PutMapping("/password/{id}")
	public ResponseEntity<UserResponse> modifyPassword(@PathVariable Long id, @Valid @RequestBody PasswordRequest request){
		return  userService.modifyPassword(id, request);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		 return userService.delete(id);
	}

	@GetMapping("/records/{search}")
	public ResponseEntity<List<UserResponse>> records(@PathVariable String search){
		return userService.records(search);
	}

	@PostMapping(value = "/profile", consumes = {"multipart/form-data"})
	public ResponseEntity<ProfileResponse> saveImage(@RequestParam("image") MultipartFile image){
		return profileService.saveProfile(image);
	}

	@GetMapping("/profile/{profileId}")
	public ResponseEntity<byte[]> getProfileImage(@PathVariable Long profileId){
		return profileService.getProfileImage(profileId);
	}

	@PutMapping(value = "profile/{id}", consumes = {"multipart/form-data"})
	public ResponseEntity<ProfileResponse> changeProfile(@PathVariable Long id, @RequestParam("image") MultipartFile image){
		return profileService.changeProfileImage(id, image);
	}
	
}