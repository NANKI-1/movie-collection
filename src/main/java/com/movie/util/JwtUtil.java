package com.movie.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public static String generateToken(Integer userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 86400000L); // 24小时

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, "movie_system_secret_key_2026")
                .compact();
    }

    public Integer getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("movie_system_secret_key_2026")
                    .parseClaimsJws(token)
                    .getBody();
            return Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("movie_system_secret_key_2026")
                    .parseClaimsJws(token)
                    .getBody();
            return (String) claims.get("username");
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey("movie_system_secret_key_2026").parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}