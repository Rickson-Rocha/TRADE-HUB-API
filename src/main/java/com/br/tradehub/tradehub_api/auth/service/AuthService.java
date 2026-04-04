package com.br.tradehub.tradehub_api.auth.service;

import com.br.tradehub.tradehub_api.auth.dto.LoginRequest;
import com.br.tradehub.tradehub_api.auth.dto.LoginResponse;
import com.br.tradehub.tradehub_api.auth.dto.RegisterRequest;
import com.br.tradehub.tradehub_api.auth.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
    void logout(String refreshToken);
    String refresh(String refreshToken);
}
