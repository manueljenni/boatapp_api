package com.manueljenni.boatapp.rest.responses;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class AuthResponse {
  @NotBlank
  @ApiModelProperty(
      value = "A Bearer token.",
      example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImV4cCI6MTYyMjUwNj"
  )
  public final String accessToken;

  @NotBlank
  public final UserResponse user;
}
