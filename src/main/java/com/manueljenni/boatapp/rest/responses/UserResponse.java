package com.manueljenni.boatapp.rest.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class UserResponse {
  @Schema(description = "The user's email address.", example = "test@gmail.com")
  public final String email;
}
