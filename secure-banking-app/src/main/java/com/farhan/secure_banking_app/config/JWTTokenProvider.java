package com.farhan.secure_banking_app.config;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;

@Component
public class JWTTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration}")
    private Long jwtExpirationDate;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername (String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    private Key key(){
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(bytes);
    }
}
