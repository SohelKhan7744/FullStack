package com.example.demo;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

  
    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkey123";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

   
    public String generateToken(String username,String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public String extractRole(String token) {
    	 return Jwts.parserBuilder()
    			 .setSigningKey(key)
    			 .build()
    			 .parseClaimsJws(token)
    			 .getBody()
             .get("role", String.class);      
    	 }
    
   

}
