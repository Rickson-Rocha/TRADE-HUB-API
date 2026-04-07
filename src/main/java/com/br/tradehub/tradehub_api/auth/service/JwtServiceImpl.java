package com.br.tradehub.tradehub_api.auth.service;

import com.br.tradehub.tradehub_api.auth.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class JwtServiceImpl  implements JwtService{

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    @Override
    public String generateAccesToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("role", user.getRole())
                .claim("username", user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

    }

    @Override
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .issuedAt(new Date())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    @Override
    public Boolean validateRefreshToken(String refreshToken) {
        try{

            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(refreshToken);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public Date getRefreshTokenExpiration() {
        return new Date(System.currentTimeMillis() + refreshTokenExpiration);
    }

    @Override
    public String extractClaim(String token, String claimName) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(claimName, String.class);
    }
}
