package com.manueljenni.boatapp.rest.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BoatResponse {
  @Schema(description = "The id of the boat", example = "1")
  Long id;

  @Schema(description = "The name of the boat", example = "My boat")
  String name;

  @Schema(description = "The description of the boat", example = "This is my boat")
  String description;

  @Schema(description = "The daily rent price of the boat", example = "1250.00")
  Float dailyPrice;

  @Schema(description = "The owner of the boat")
  UserResponse owner;
}
