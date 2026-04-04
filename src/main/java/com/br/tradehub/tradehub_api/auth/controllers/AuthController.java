package com.br.tradehub.tradehub_api.auth.controllers;


import com.br.tradehub.tradehub_api.auth.dto.LoginRequest;
import com.br.tradehub.tradehub_api.auth.dto.LoginResponse;
import com.br.tradehub.tradehub_api.auth.dto.RegisterRequest;
import com.br.tradehub.tradehub_api.auth.dto.RegisterResponse;
import com.br.tradehub.tradehub_api.auth.service.AuthService;
import com.br.tradehub.tradehub_api.auth.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/trade-hub/auth")
@RequiredArgsConstructor
public class AuthController {

    private  final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public EntityModel<RegisterResponse> register(@RequestBody @Valid RegisterRequest registerRequest){
         RegisterResponse response = authService.register(registerRequest);
        return EntityModel.of(
                response,
                linkTo(methodOn(AuthController.class).register(null)).withSelfRel(),
                linkTo(methodOn(AuthController.class).login(null)).withRel("login")
        );

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Refresh-Token") String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

}
