package com.manueljenni.boatapp.services;

import com.manueljenni.boatapp.entities.User;
import com.manueljenni.boatapp.repositories.UserRepo;
import com.manueljenni.boatapp.rest.requests.SignUpRequest;
import com.manueljenni.boatapp.rest.responses.UserResponse;
import java.security.Principal;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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

  public User findUser(Principal principal) {
    final Long userId = getUserId(principal);
    final var userOptional = userRepo.findById(userId);
    if (userOptional.isEmpty()) {
      log.warn("User auth not found, user does not exist with id " + userId);
    }
    return userOptional.orElseThrow(RuntimeException::new);
  }

  public Long getUserId(Principal principal) {
    final UsernamePasswordAuthenticationToken token =
        (UsernamePasswordAuthenticationToken) principal;
    if (token == null) {
      log.warn("User auth not found, principal is null");
      throw new RuntimeException("User auth not found, principal is null");
    }
    final UserPrincipal user = (UserPrincipal) token.getPrincipal();
    return user.getId();
  }

  public UserResponse getMe(User user) {
    return convertEntityToResponse(user);
  }

  private UserResponse convertEntityToResponse(User user) {
    return UserResponse.builder()
        .email(user.getEmail())
        .build();
  }
}
