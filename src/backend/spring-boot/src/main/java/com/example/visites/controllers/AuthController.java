package com.example.visites.controllers;


import com.example.visites.dto.SignInRequest;
import com.example.visites.dto.SignResponse;
import com.example.visites.dto.UserRequest;
import com.example.visites.dto.UserResponse;
import com.example.visites.exceptions.ResourceNotFoundException;
import com.example.visites.exceptions.UserNotFoundException;
import com.example.visites.manager.JWTUtil;
import com.example.visites.models.User;
import com.example.visites.repositories.UserRepository;
import com.example.visites.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@SecurityRequirement(name = "Tracking Application")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	public AuthController() {
	}

	@PostMapping("/register")
	public ResponseEntity<SignResponse> register(@Valid @RequestBody UserRequest user) throws UserNotFoundException {

		modelMapper.map(userService.create(user), UserResponse.class);

		SignInRequest login = new SignInRequest(
				user.getEmail(), user.getPassword()
		);
		// Map<String, Object> token = login(login);

		// SignResponse resp = new SignResponse(userResponse, token);

		// return new ResponseEntity<>(resp,
		// 		HttpStatus.CREATED);
		return login(login);
	}

	@PostMapping("/login")
	public ResponseEntity<SignResponse> login(@Valid @RequestBody SignInRequest request) {

		UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
				request.getEmail(), request.getPassword());

		authenticationManager.authenticate(authCredentials);

		String token = jwtUtil.generateToken(request.getEmail());

		User user = userRepository.findByEmailOrUsername(request.getEmail(), request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("L'User", "d'Email", request.getEmail()));
		UserResponse userResponse = modelMapper.map(user, UserResponse.class);

		SignResponse resp = new SignResponse(userResponse, token);
		return new ResponseEntity(resp, HttpStatus.ACCEPTED);
	}
}