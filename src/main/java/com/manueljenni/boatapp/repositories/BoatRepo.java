package com.manueljenni.boatapp.repositories;

import com.manueljenni.boatapp.entities.Boat;
import com.manueljenni.boatapp.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoatRepo extends JpaRepository<Boat, Long> {

  Optional<Boat> findById(Long id);

  Optional<Boat> findByIdAndOwner(Long id, User owner);

  Optional<Boat> findByName(String name);

  List<Boat> findAllByOwner(User owner);

  int deleteByIdAndOwner(Long id, User owner);
}
