package com.manueljenni.boatapp.rest;

import com.manueljenni.boatapp.entities.Boat;
import com.manueljenni.boatapp.entities.User;
import com.manueljenni.boatapp.repositories.BoatRepo;
import com.manueljenni.boatapp.rest.requests.CreateBoatRequest;
import com.manueljenni.boatapp.rest.requests.UpdateBoatRequest;
import com.manueljenni.boatapp.rest.responses.ApiResponse;
import com.manueljenni.boatapp.rest.responses.BoatResponse;
import com.manueljenni.boatapp.services.BoatService;
import com.manueljenni.boatapp.services.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boat")
public class BoatResource {

  @Autowired
  private UserService userService;

  @Autowired
  private BoatRepo boatRepo;

  @Autowired
  private BoatService boatService;

  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "201",
      description = "Boat successfully created"
  )
  @PostMapping("/create")
  public ResponseEntity<Boat> createBoat(
      @Valid @RequestBody CreateBoatRequest createBoatRequest,
      Principal principal
  ) {
    final User user = userService.findUser(principal);
    return new ResponseEntity<>(boatService.createBoat(createBoatRequest, user),
        HttpStatus.CREATED);
  }

  @PostMapping("/{id}/update")
  public ResponseEntity<Boat> updateBoat(
      @PathVariable("id") Long id,
      @Valid @RequestBody UpdateBoatRequest updateBoatRequest,
      Principal principal
  ) {
    final User user = userService.findUser(principal);
    // Check if the boat exists and belongs to the user
    Boat boat = boatRepo.findByIdAndOwner(id, user)
        .orElseThrow(() -> new RuntimeException("Boat not found"));
    return new ResponseEntity<>(boatService.updateBoat(boat, updateBoatRequest),
        HttpStatus.OK);
  }

  @DeleteMapping("/{id}/delete")
  public ResponseEntity<ApiResponse> deleteBoat(@PathVariable("id") Long id, Principal principal) {
    final User user = userService.findUser(principal);
    var rowsDeleted = boatService.deleteBoat(id, user);
    if (rowsDeleted > 0) {
      return ResponseEntity.ok(new ApiResponse(String.format("Boat with id %s deleted", id)));
    } else {
      return ResponseEntity.internalServerError()
          .body(new ApiResponse(String.format("Boat with id %s could not be deleted", id)));
    }
  }

  @GetMapping("/allBoats")
  public ResponseEntity<List<BoatResponse>> getAllBoats(Principal principal) {
    final User user = userService.findUser(principal);
    return new ResponseEntity<>(boatService.getAllBoats(user), HttpStatus.OK);
  }
}
