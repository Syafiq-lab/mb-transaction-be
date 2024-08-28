package com.mb.transactionbackend.controller;

import com.mb.transactionbackend.dto.ApiResponse;
import com.mb.transactionbackend.dto.AuthRequest;
import com.mb.transactionbackend.dto.AuthResponse;
import com.mb.transactionbackend.dto.UserRegistrationRequest;
import com.mb.transactionbackend.model.User;
import com.mb.transactionbackend.service.JwtUserDetailsService;
import com.mb.transactionbackend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authRequest.getUsername(), authRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect username or password", e);
		}

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthResponse(token));
	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<User>> registerUser(@RequestBody UserRegistrationRequest request) {
		User newUser = new User();
		newUser.setUsername(request.getUsername());
		newUser.setPassword(request.getPassword());  // Password will be hashed in the service layer

		try {
			User savedUser = userDetailsService.save(newUser);
			return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", savedUser));
		} catch (Exception e) {
			throw new RuntimeException("User registration failed", e);  // This will be caught by the global exception handler
		}
	}

}
