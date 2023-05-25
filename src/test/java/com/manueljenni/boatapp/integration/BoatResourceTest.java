package com.manueljenni.boatapp.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.manueljenni.boatapp.BoatappApplication;
import com.manueljenni.boatapp.Utils;
import com.manueljenni.boatapp.entities.Boat;
import com.manueljenni.boatapp.entities.User;
import com.manueljenni.boatapp.repositories.BoatRepo;
import com.manueljenni.boatapp.repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = BoatappApplication.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class BoatResourceTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private BoatRepo boatRepo;
  @Autowired
  private UserRepo userRepo;
  @Value("${jwt.secret}")
  private String jwtSecret;

  @BeforeEach
  void setUp() {
    boatRepo.deleteAll();
    userRepo.deleteAll();
  }

  @Test
  @Transactional
  void test_createBoat() throws Exception {
    var user = userRepo.save(User.builder()
        .email("test_createBoat@test.com")
        .password("testtest")
        .build());

    // Boat is created
    this.mockMvc.perform(
            post("/api/v1/boat/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "My boat",
                      "description": "This is my boat",
                      "dailyPrice": 1250
                    }
                    """)
                .header(
                    "authorization",
                    "Bearer " + Utils.getJwtToken(jwtSecret, user.getId().toString())
                )
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("My boat"))
        .andExpect(jsonPath("$.description").value("This is my boat"))
        .andExpect(jsonPath("$.dailyPrice").value(1250))
        .andExpect(jsonPath("$.owner.email").value(user.getEmail()))
    ;

    // Boat is saved to DB
    var boats = boatRepo.findAllByOwner(user);
    assertEquals(1, boats.size());
    assertEquals("My boat", boats.get(0).getName());
    assertEquals("This is my boat", boats.get(0).getDescription());
    assertEquals(1250, boats.get(0).getDailyPrice());

    // The user is the owner
    assertEquals(user, boats.get(0).getOwner());
  }

  @Test
  void test_getBoatById() throws Exception {
    var user = userRepo.save(User.builder()
        .email("test_getBoatById@test.com")
        .password("testtest")
        .build());

    var boat = boatRepo.save(Boat.builder()
        .name("A new boat")
        .description("Has been saved to the DB")
        .dailyPrice(999f)
        .owner(user)
        .build());

    // Boat is returned
    this.mockMvc.perform(
            get("/api/v1/boat/" + boat.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                    "authorization",
                    "Bearer " + Utils.getJwtToken(jwtSecret, user.getId().toString())
                )
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(boat.getId()))
        .andExpect(jsonPath("$.name").value("A new boat"))
        .andExpect(jsonPath("$.description").value("Has been saved to the DB"))
        .andExpect(jsonPath("$.dailyPrice").value(999))
        .andExpect(jsonPath("$.owner.email").value(user.getEmail()))
    ;
  }

  @Test
  @Transactional
  void test_updateBoat() throws Exception {
    var user = userRepo.save(User.builder()
        .email("test_updateBoat@test.com")
        .password("testtest")
        .build());

    var boat = boatRepo.save(Boat.builder()
        .name("A new boat")
        .description("Has been saved to the DB")
        .dailyPrice(999f)
        .owner(user)
        .build());

    // Update the boat
    this.mockMvc.perform(
            patch("/api/v1/boat/" + boat.getId() + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "description": "The description has been updated!"
                    }
                    """)
                .header(
                    "authorization",
                    "Bearer " + Utils.getJwtToken(jwtSecret, user.getId().toString())
                )
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(boat.getId()))
        .andExpect(jsonPath("$.name").value("A new boat"))
        .andExpect(jsonPath("$.description").value("The description has been updated!"))
        .andExpect(jsonPath("$.dailyPrice").value(999))
        .andExpect(jsonPath("$.owner.email").value(user.getEmail()))
    ;

    var newBoat = boatRepo.findById(boat.getId()).get();
    // The description has been updated in the DB
    assertEquals("The description has been updated!", newBoat.getDescription());
    // The name and dailyPrice are still the same
    assertEquals("A new boat", newBoat.getName());
    assertEquals(999, newBoat.getDailyPrice());
  }

  @Test
  @Transactional
  void test_deleteBoat() throws Exception {
    var user = userRepo.save(User.builder()
        .email("test_deleteBoat@test.com")
        .password("testtest")
        .build());

    var boat = boatRepo.save(Boat.builder()
        .name("A new boat")
        .description("Has been saved to the DB")
        .dailyPrice(999f)
        .owner(user)
        .build());

    // Delete the boat
    this.mockMvc.perform(
            delete("/api/v1/boat/" + boat.getId() + "/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                    "authorization",
                    "Bearer " + Utils.getJwtToken(jwtSecret, user.getId().toString())
                )
        )
        .andDo(print())
        .andExpect(status().isOk());

    // No boats left in DB
    var boats = boatRepo.findAllByOwner(user);
    assertEquals(0, boats.size());
  }

  @Test
  @Transactional
  void test_deleteBoat__cantDeleteOtherBoats() throws Exception {
    // Given two users
    var user = userRepo.save(User.builder()
        .email("test_deleteBoat__cantDeleteOtherBoats@test.com")
        .password("testtest")
        .build());

    var otherUser = userRepo.save(User.builder()
        .email("test_deleteBoat__cantDeleteOtherBoats_otherUser@test.com")
        .password("testtest")
        .build());

    // User saves the boat
    var boat = boatRepo.save(Boat.builder()
        .name("A new boat")
        .description("Has been saved to the DB")
        .dailyPrice(999f)
        .owner(user)
        .build());

    // Other user tries to delete the boat, but fails
    this.mockMvc.perform(
            delete("/api/v1/boat/" + boat.getId() + "/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                    "authorization",
                    "Bearer " + Utils.getJwtToken(jwtSecret, otherUser.getId().toString())
                )
        )
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("Boat could not be deleted"));

    // The boat is still saved in the DB
    var boats = boatRepo.findAllByOwner(user);
    assertEquals(1, boats.size());
    assertEquals(boat, boats.get(0));
  }
}
