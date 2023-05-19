package com.manueljenni.boatapp.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class CustomException extends RuntimeException {
  @Schema(description = "Human readable error")
  public String message;

  @Schema(description = "Status code of the error")
  public HttpStatus statusCode;
}
