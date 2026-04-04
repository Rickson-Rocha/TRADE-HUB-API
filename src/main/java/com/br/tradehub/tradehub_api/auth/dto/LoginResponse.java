package com.br.tradehub.tradehub_api.auth.dto;

import com.br.tradehub.tradehub_api.auth.enums.Role;
import com.br.tradehub.tradehub_api.auth.enums.Status;

public record LoginResponse(
        String name,
        Role role,
        Status status,
        String accessToken,
        String refreshToken
) {
}
