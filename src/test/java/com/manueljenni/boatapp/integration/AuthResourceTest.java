package com.manueljenni.boatapp.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.manueljenni.boatapp.BoatappApplication;
import com.manueljenni.boatapp.entities.User;
import com.manueljenni.boatapp.repositories.BoatRepo;
import com.manueljenni.boatapp.repositories.UserRepo;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = BoatappApplication.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class AuthResourceTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserRepo userRepo;
  @Autowired
  private BoatRepo boatRepo;

  @BeforeEach
  void setUp() {
    boatRepo.deleteAll();
    userRepo.deleteAll();
  }

  @Test
  void test_signup() throws Exception {
    this.mockMvc.perform(
            post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"email\": \"test_signup@test.com\"," +
                    "\"password\": \"test\"" +
                    "}"
                )
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").isString());

    // A user should have been created in the database
    List<User> users = userRepo.findAll();
    assertEquals(1, users.size());
    assertEquals("test_signup@test.com", users.get(0).getEmail());
    assertEquals("test", users.get(0).getPassword());
  }

  @Test
  void test_login() throws Exception {
    // Save a user to DB
    final var user = userRepo.save(User.builder()
        .email("test_login@test.com")
        .password("test")
        .build()
    );

    // User can login
    this.mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"email\": \"test_login@test.com\"," +
                    "\"password\": \"test\"" +
                    "}"
                ))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").isString());
  }

  @Test
  void test_unauthorized() throws Exception {
    // No user saved to DB, but tries to login
    this.mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"email\": \"test_signup@test.com\"," +
                    "\"password\": \"test\"" +
                    "}"
                ))
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Login failed!"));
  }
}
