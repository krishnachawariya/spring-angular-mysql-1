package com.example.springangular.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springangular.dto.AuthenticationResponse;
import com.example.springangular.dto.LoginRequest;
import com.example.springangular.dto.RegisterRequest;
import com.example.springangular.exceptions.SpringRedditException;
import com.example.springangular.model.NotificationEmail;
import com.example.springangular.model.User;
import com.example.springangular.model.VerificationToken;
import com.example.springangular.repository.UserRepository;
import com.example.springangular.repository.VerificationTokenRepository;
import com.example.springangular.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	
	

	@Transactional
	public void signup(RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		
		userRepository.save(user);
		String token = generateVerificationToken(user);
		
		 mailService.sendMail(new NotificationEmail("Please Activate your Account",
	                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
	                "please click on the below url to activate your account : " +
	                "http://localhost:8081/api/auth/accountVerification/" + token));
	}
	
	
	private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
		fetchUserAndEnable(verificationToken.get());
		
	}
	
	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
		
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		 System.out.println("Coming to Service...");
		 Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
	                loginRequest.getPassword()));
	        SecurityContextHolder.getContext().setAuthentication(authenticate);
	        String token = jwtProvider.generateToken(authenticate);
	        return new AuthenticationResponse(token, loginRequest.getUsername());
			/*
			 * return AuthenticationResponse.builder() .authenticationToken(token)
			 * //.refreshToken(refreshTokenService.generateRefreshToken().getToken())
			 * //.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis())
			 * ) .username(loginRequest.getUsername()) .build();
			 */
	}
	
	 @Transactional(readOnly = true)
	 public User getCurrentUser() {
		 org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
	                getContext().getAuthentication().getPrincipal();
	     return userRepository.findByUsername(principal.getUsername())
	                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
	    }


}

