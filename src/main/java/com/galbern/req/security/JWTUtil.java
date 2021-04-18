package com.galbern.req.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    private String SECRET_KEY = "EBA6C1E853928762A48B68A58DD9E7D9EA2CEB16DD3357AE45881F5AB1";

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact(); // add roles in authzn
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date getExpiryDate(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Boolean isExpired(String token) {
        return getExpiryDate(token).before(new Date());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody(); //.build().parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }
}
