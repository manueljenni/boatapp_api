package com.manueljenni.boatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
  // Enable CORS
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:3031", "https://boatapp.vercel.app/")
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .allowedHeaders("*");
  }
}
