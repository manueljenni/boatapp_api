package com.manueljenni.boatapp.config;

import com.manueljenni.boatapp.security.JwtAuthenticationFilter;
import com.manueljenni.boatapp.security.JwtEntryPoint;
import com.manueljenni.boatapp.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

  @Autowired
  CustomUserDetailsService customUserDetailsService;
  @Autowired
  private JwtEntryPoint unauthorizedHandler;

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
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
        // Allow anonymous access to these routes:
        .requestMatchers(
            // Swagger
            "/swagger-ui/**",
            "/v3/api-docs/**",
            // Sign up and login
            "/authentication/**"
        ).permitAll()
        .anyRequest().authenticated();
    return http.build();
  }
}