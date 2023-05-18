package com.manueljenni.boatapp.rest;

import com.manueljenni.boatapp.rest.responses.UserResponse;
import com.manueljenni.boatapp.services.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserResource {

  @Autowired
  private UserService userService;

  @GetMapping("/me")
  public UserResponse getMe(Principal principal) {
    final var user = userService.findUser(principal);
    return userService.getMe(user);
  }
}
