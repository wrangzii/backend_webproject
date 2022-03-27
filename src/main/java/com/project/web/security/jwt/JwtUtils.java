package com.project.web.security.jwt;

import com.project.web.payload.response.ResponseObject;
import com.project.web.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtUtils {
    @Value("${project.web.jwtSecret}")
    private String jwtSecret;
    @Value("${project.web.jwtExpirationMs}")
    private int jwtExpirationMs;
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .claim("username", userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("username",String.class);
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject("Invalid JWT signature: {}", e.getMessage()));
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject("Invalid JWT token: {}", e.getMessage()));
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject("Invalid JWT token: {}", e.getMessage()));
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject("JWT token is unsupported: {}", e.getMessage()));
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject("JWT claims string is empty: {}", e.getMessage()));
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
