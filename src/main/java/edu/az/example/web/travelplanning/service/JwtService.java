package edu.az.example.web.travelplanning.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "bC6hOCv7SZEbNmvFqlP9+jPdeuvztv2mx3Aw77Zk2mY=";

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(); // for adding custom data into payload

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .signWith(getKey())
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims); // for pulling out specific claim
    }

    public Claims extractAllClaims(String token){
        return Jwts
                .parser() // create parser(decoding the JWT)
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token) //decode & verify token
                .getPayload();
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token){
        Date date = extractClaim(token, Claims::getExpiration);
        return date.before(new Date());
    }

    public SecretKey getKey() {
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

}
