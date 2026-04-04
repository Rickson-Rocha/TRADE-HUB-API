package com.br.tradehub.tradehub_api.auth.service;
import com.br.tradehub.tradehub_api.auth.dto.RegisterResponse;
import com.br.tradehub.tradehub_api.auth.entity.RefreshToken;
import com.br.tradehub.tradehub_api.auth.repository.RefreshTokenRepository;
import com.br.tradehub.tradehub_api.auth.repository.UserRepository;
import com.br.tradehub.tradehub_api.auth.dto.LoginRequest;
import com.br.tradehub.tradehub_api.auth.dto.LoginResponse;
import com.br.tradehub.tradehub_api.auth.dto.RegisterRequest;
import com.br.tradehub.tradehub_api.auth.entity.User;
import com.br.tradehub.tradehub_api.auth.enums.Role;
import com.br.tradehub.tradehub_api.auth.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl  implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User newUser = new User();
        newUser.setEmail(registerRequest.email());
        newUser.setPassword( passwordEncoder.encode(registerRequest.password()));
        newUser.setUsername(registerRequest.name());
        newUser.setRole(Role.CUSTOMER);
        newUser.setStatus(Status.ACTIVE);
        newUser.setCreatedAt(LocalDateTime.now());
        userRepository.save(newUser);

        return new RegisterResponse(registerRequest.name(), registerRequest.email());

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email()).
                orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));


        if(!passwordEncoder.matches(loginRequest.password(), user.getPassword())){
            throw new IllegalArgumentException("Invalid credentials");
        }

        if(user.getStatus()!=Status.ACTIVE){
            throw new IllegalArgumentException("User is not active");
        }

        String accessToken = jwtService.generateAccesToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiratedAt(jwtService.getRefreshTokenExpiration());
        refreshTokenEntity.setRevoked(false);

        refreshTokenRepository.save(refreshTokenEntity);

        return  new LoginResponse(user.getUsername(), user.getRole(), user.getStatus(), accessToken, refreshToken);
    }

    @Override
    public void logout(String refreshToken) {
        RefreshToken verifyRefreshToken = refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
        verifyRefreshToken.setRevoked(true);
        refreshTokenRepository.save(verifyRefreshToken);
    }

    @Override
    public String refresh(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByTokenAndRevokedFalse(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or revoked refresh token"));
        return jwtService.generateAccesToken(token.getUser());
    }

}
