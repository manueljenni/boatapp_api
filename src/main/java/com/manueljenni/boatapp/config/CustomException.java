package com.manueljenni.boatapp.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class CustomException extends RuntimeException {
  @Schema(description = "Human readable error")
  public String message;

  @Schema(description = "Status code of the error")
  public HttpStatus statusCode;
}
