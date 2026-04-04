package com.br.tradehub.tradehub_api.auth.service;

import com.br.tradehub.tradehub_api.auth.entity.User;

import java.util.Date;

public interface JwtService {
    String generateAccesToken(User user);
    String generateRefreshToken(User user);
    Boolean validateRefreshToken(String refreshToken);
    String extractUsername(String token);
    Date getRefreshTokenExpiration();

}
