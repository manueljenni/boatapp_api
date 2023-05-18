package com.manueljenni.boatapp.repositories;

import com.manueljenni.boatapp.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  Optional<User> findByEmailAndPassword(String email, String password);

  Boolean existsByEmail(String email);
}
