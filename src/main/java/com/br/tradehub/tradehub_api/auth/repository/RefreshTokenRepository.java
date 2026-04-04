package com.br.tradehub.tradehub_api.auth.repository;

import com.br.tradehub.tradehub_api.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);

    @Query("SELECT rt FROM RefreshToken rt WHERE rt.token = :token AND rt.revoked = false AND rt.expiratedAt > CURRENT_TIMESTAMP")
    Optional<RefreshToken> findByTokenAndRevokedFalse(@Param("token") String token);
}
