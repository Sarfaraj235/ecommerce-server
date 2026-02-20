package com.app.controller;

import org.apache.coyote.http11.Http11InputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.config.JwtService;
import com.app.dto.AuthResponse;
import com.app.dto.LoginRequest;
import com.app.exceptions.UserException;
import com.app.pojos.User;
import com.app.repository.UserRepository;
import com.app.service.CustomUserServiceImpl;

@RestController
@RequestMapping("/auth")

public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtservice;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomUserServiceImpl customUserService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUser(@RequestBody User user) throws UserException {

		String email = user.getEmail();
		String password = user.getPassword();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();

		User isEmailExists = userRepo.findByEmail(email);

		if (isEmailExists != null) {
			throw new UserException("Email is Already Used With Another Account !!!");
		}

		User createdUser = new User(email, passwordEncoder.encode(password), firstName, lastName);

		User savedUser = userRepo.save(createdUser);

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(email, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtservice.generateToken(authentication);

		return new ResponseEntity<AuthResponse>(new AuthResponse(token, "SignUp successful"), HttpStatus.CREATED);

	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

		String email = request.getEmail();
		String password = request.getPassword();

		Authentication authentication = authenticate(email, password);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtservice.generateToken(authentication);

		return new ResponseEntity<AuthResponse>(new AuthResponse(token, "Login successful"), HttpStatus.CREATED);
	}

	private Authentication authenticate(String email, String password) {
		UserDetails userDetails = customUserService.loadUserByUsername(email);

		if (userDetails == null) {
			throw new BadCredentialsException("invalid username !!!");
		}

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {

			throw new BadCredentialsException("invalid Password !!!");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}