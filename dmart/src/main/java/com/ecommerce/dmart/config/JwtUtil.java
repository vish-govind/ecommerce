package com.ecommerce.dmart.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	 //Move it to secrets 
	 private final String secret = "Hol45^mpo#27ba)";
	 
	 public String generateToken(String username) {
	        Map<String, Object> claims = new HashMap<>();
	        return createToken(claims, username);
	    }

	public String createToken(Map<String, Object> claims, String username)
	{
		 return Jwts.builder()
	                .setClaims(claims) // Custom claims (optional)
	                .setSubject(username) //User name
	                .setIssuedAt(new Date(System.currentTimeMillis())) // Issue date
	                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiry: 10 hours
	                .signWith(SignatureAlgorithm.HS256, secret) // Signing with secret key
	                .compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }
	
	private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

}
