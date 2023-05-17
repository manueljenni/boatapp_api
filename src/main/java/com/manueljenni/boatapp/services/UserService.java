package com.manueljenni.boatapp.services;

import com.manueljenni.boatapp.entities.User;
import com.manueljenni.boatapp.repositories.UserRepo;
import com.manueljenni.boatapp.rest.requests.SignUpRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepo userRepo;

  public Optional<User> createUser(SignUpRequest signUpRequest) {
    return Optional.of(userRepo.save(
        new User(
            signUpRequest.getEmail(),
            signUpRequest.getPassword()
        ))
    );
  }
}
