package com.manueljenni.boatapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException e
  ) throws IOException {
    log.error("Responding with unauthorized error. Message - {} {} {}",
        request.getMethod(),
        request.getRequestURI().substring(request.getContextPath().length()),
        e.getMessage()
    );
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
  }
}
