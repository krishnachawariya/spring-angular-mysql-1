package com.example.springangular.controller;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springangular.dto.AuthenticationResponse;
import com.example.springangular.dto.LoginRequest;
import com.example.springangular.dto.RegisterRequest;
import com.example.springangular.service.AuthService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		
		authService.signup(registerRequest);
		return new ResponseEntity<>("User Registration Successful", OK);
		
	}
	
	@GetMapping("accountVerification/{token}")
	public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }
	
	 @PostMapping("/login")
	 public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
		 System.out.println("Coming to controller...");
	     return authService.login(loginRequest);
	 }
	
	

}
