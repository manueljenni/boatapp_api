package com.manueljenni.boatapp.rest.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class AuthResponse {
  @NotBlank
  @Schema(description = "Bearer access token")
  public final String accessToken;

  @NotBlank
  public final UserResponse user;
}
