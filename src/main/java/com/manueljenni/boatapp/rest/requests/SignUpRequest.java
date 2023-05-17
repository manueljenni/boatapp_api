package com.manueljenni.boatapp.rest.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequest {
  @NotBlank(message = "Email can't be blank.")
  @Email
  private String email;

  @NotBlank(message = "Password can't be blank.")
  private String password;
}
