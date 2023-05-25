package com.manueljenni.boatapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();
    // TODO: This seems to be returned also in case where the login is not the problem
    // (e.g. when a field is missing when creating a boat)
    writer.println("{\"message\": \"You need to be logged in.\"}");
    writer.flush();
  }
}
