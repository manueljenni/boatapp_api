package com.manueljenni.boatapp.rest.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateBoatRequest {
  @Schema(description = "The name of the boat", example = "My boat")
  @NotNull
  String name;

  @Schema(description = "The description of the boat", example = "This is my boat")
  String description;

  @Schema(description = "The daily rent price of the boat", example = "1250.00")
  Float dailyPrice;
}
