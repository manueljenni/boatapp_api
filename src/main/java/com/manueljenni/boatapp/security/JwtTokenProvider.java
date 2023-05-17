package com.manueljenni.boatapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

@Slf4j
@Component
public class JwtTokenProvider {
  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expirationInYears}")
  private int jwtExpirationInYears;

  public String generateToken(Long userId) {
    final Date expiryDate = Date.from(LocalDateTime.now().plusYears(jwtExpirationInYears)
        .atZone(ZoneId.systemDefault()).toInstant());

    return Jwts.builder()
        .setSubject(Long.toString(userId))
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public Long getUserIdFromJWT(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody();

    return Long.parseLong(claims.getSubject());
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      log.error("Invalid JWT signature");
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token");
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty.");
    }
    return false;
  }
}
