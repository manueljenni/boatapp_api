package com.manueljenni.boatapp;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class Utils {
  public static String getJwtToken(String jwtSecret, String userId) {
    return Jwts.builder()
        .setSubject(userId)
        .setIssuedAt(new Date())
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }
}
