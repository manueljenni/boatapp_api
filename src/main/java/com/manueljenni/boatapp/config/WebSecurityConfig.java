package com.manueljenni.boatapp.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.manueljenni.boatapp.security.JwtAuthenticationFilter;
import com.manueljenni.boatapp.security.JwtEntryPoint;
import com.manueljenni.boatapp.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

  @Autowired
  private JwtEntryPoint unauthorizedHandler;

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
  }

  @Autowired
  CustomUserDetailsService customUserDetailsService;

  public DaoAuthenticationProvider passwordAuthenticationProvider() {
    final var authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(customUserDetailsService);
    return authenticationProvider;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors().and().csrf().disable()
        // Handle unauthorized requests
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        // Don't create sessions - I'll use JWTs
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeHttpRequests()
        // All requests to /authentication/** are allowed - so users can log in and register
        .requestMatchers("/authentication/**").permitAll()
        .anyRequest().authenticated();
    return http.build();
  }
}