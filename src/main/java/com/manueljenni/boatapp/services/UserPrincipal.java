package com.manueljenni.boatapp.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manueljenni.boatapp.entities.User;
import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UserPrincipal implements UserDetails {
  private final Long id;

  @JsonIgnore
  private final String email;

  @JsonIgnore
  private final String password;

  private final Collection<? extends GrantedAuthority> authorities;

  public UserPrincipal(
      Long id,
      String email,
      String password,
      Collection<? extends GrantedAuthority> authorities
  ) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserPrincipal create(User user) {
    return new UserPrincipal(
        user.getId(),
        user.getEmail(),
        user.getPassword(),
        // TODO: Add roles
        // For now, we don't have any, but those could be added here (admin, user, etc.)
        Collections.emptyList()
    );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  // Spring security requires a username for authentication
  // For now we use the email as username
  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
