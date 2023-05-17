package com.manueljenni.boatapp.rest;

import com.manueljenni.boatapp.entities.User;
import com.manueljenni.boatapp.repositories.UserRepo;
import com.manueljenni.boatapp.rest.requests.SignUpRequest;
import com.manueljenni.boatapp.rest.responses.AuthResponse;
import com.manueljenni.boatapp.rest.responses.UserResponse;
import com.manueljenni.boatapp.security.JwtTokenProvider;
import com.manueljenni.boatapp.services.UserService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthResource {

  @Autowired
  JwtTokenProvider tokenProvider;
  @Autowired
  private UserRepo userRepo;
  @Autowired
  private UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
    if (userRepo.existsByEmail(signUpRequest.getEmail())) {
      return new ResponseEntity("Email Address already in use!", HttpStatus.BAD_REQUEST);
    }
    // Create new user
    final Optional<User> userOptional = userService.createUser(signUpRequest);
    if (userOptional.isPresent()) {
      // If successful, generate JWT
      final User user = userOptional.get();
      final String jwt = tokenProvider.generateToken(user.getId());
      return ResponseEntity.ok(AuthResponse.builder()
          .accessToken(jwt)
          .user(UserResponse.builder()
              .email(user.getEmail())
              .build())
          .build());
    } else {
      return new ResponseEntity("Could not create user!", HttpStatus.BAD_REQUEST);
    }
  }
}