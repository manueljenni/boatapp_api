package com.manueljenni.boatapp.security;

import com.manueljenni.boatapp.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  JwtTokenProvider jwtTokenProvider;

  @Autowired
  CustomUserDetailsService customUserDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      final String jwt = getJwtFromRequest(request);

      // Check if the JWT is valid
      if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
        // Get the user ID from the JWT, and use that to fetch the user details
        final Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
        final UserDetails userDetails = customUserDetailsService.loadUserById(userId);
        final UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception ex) {
      logger.error("Could not set user authentication in security context", ex);
    }
    filterChain.doFilter(request, response);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    final String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null) {
      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
        return bearerToken.substring(7);
      } else {
        final var logToken = bearerToken.substring(0, 10) + "****";
        logger.warn("Invalid authorization header: " + logToken);
      }
    }
    return null;
  }
}
