package com.example.lastproject.service;

import com.example.lastproject.model.User;
import com.example.lastproject.model.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


@Service
public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000;

    public static String generateToken(String email, Role role,String id) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("id",id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
    public static String extractEmail(String token){

        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
    public static String extractRole(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("role", String.class);
    }
    public static String extractId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("id", String.class);
    }

    public static boolean validateToken(String token, User user){
        if(user.getRole().name().equals(extractRole(token))&&
                user.getEmail().equals(extractEmail(token))&&
                !isExpired(token))
           return true;
        return false;
    }
    public static boolean validateToken(String token){
    return true;
    }
    public static boolean isExpired(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}