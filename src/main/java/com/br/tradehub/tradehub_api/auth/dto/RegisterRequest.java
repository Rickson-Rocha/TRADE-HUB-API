package com.br.tradehub.tradehub_api.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(

         @NotBlank(message = "Name is required")
         String name,
         @Email(message = "Invalid email format")
         String email,
         @NotBlank(message = "password is required")
         String password
) {
}
