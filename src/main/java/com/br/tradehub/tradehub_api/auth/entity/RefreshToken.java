package com.br.tradehub.tradehub_api.auth.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tb_refresh_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String token;

    private Date expiratedAt;

    private Boolean revoked = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
