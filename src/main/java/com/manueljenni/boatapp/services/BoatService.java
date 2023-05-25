package com.manueljenni.boatapp.services;

import com.manueljenni.boatapp.entities.Boat;
import com.manueljenni.boatapp.entities.User;
import com.manueljenni.boatapp.repositories.BoatRepo;
import com.manueljenni.boatapp.rest.requests.CreateBoatRequest;
import com.manueljenni.boatapp.rest.requests.UpdateBoatRequest;
import com.manueljenni.boatapp.rest.responses.BoatResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoatService {

  @Autowired
  private BoatRepo boatRepo;

  @Autowired
  private UserService userService;

  public BoatResponse createBoat(CreateBoatRequest createBoatRequest, User user) {
    final var boat = Boat.builder()
        .name(createBoatRequest.getName())
        .description(createBoatRequest.getDescription())
        .dailyPrice(createBoatRequest.getDailyPrice())
        .owner(user)
        .build();
    return mapEntityToResponse(boatRepo.save(boat), user);
  }

  public BoatResponse updateBoat(Boat boat, UpdateBoatRequest updateBoatRequest, User user) {
    // Update boat - only set new values if they are present
    if (updateBoatRequest.getName() != null) {
      boat.setName(updateBoatRequest.getName());
    }
    if (updateBoatRequest.getDescription() != null) {
      boat.setDescription(updateBoatRequest.getDescription());
    }
    if (updateBoatRequest.getDailyPrice() != null) {
      boat.setDailyPrice(updateBoatRequest.getDailyPrice());
    }

    // Save the updated boat to the DB
    return mapEntityToResponse(boatRepo.save(boat), user);
  }

  public int deleteBoat(Long boatId, User user) {
    return boatRepo.deleteByIdAndOwner(boatId, user);
  }

  public List<BoatResponse> getAllBoats(User user) {
    return boatRepo.findAllByOwner(user).stream()
        .map(boat -> mapEntityToResponse(boat, user))
        .toList();
  }

  // Helper method to map the entity to the response
  // Useful if fields will be added in the future
  public BoatResponse mapEntityToResponse(Boat boat, User owner) {
    return BoatResponse.builder()
        .id(boat.getId())
        .name(boat.getName())
        .description(boat.getDescription())
        .dailyPrice(boat.getDailyPrice())
        .owner(userService.mapEntityToResponse(owner))
        .build();
  }
}
