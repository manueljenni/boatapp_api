package com.manueljenni.boatapp.rest.responses;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class UserResponse {
  @ApiModelProperty(value = "The user's email address.", example = "test@gmail.com")
  public final String email;
}
