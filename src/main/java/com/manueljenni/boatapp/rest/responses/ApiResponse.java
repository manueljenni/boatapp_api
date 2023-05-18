package com.manueljenni.boatapp.rest.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ApiResponse {

  @Schema(description = "Human readable message")
  public String message;
}
