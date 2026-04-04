CREATE TABLE tb_refresh_tokens
(
    id           UUID NOT NULL,
    token        VARCHAR(255) NOT NULL,
    expirated_at TIMESTAMP WITHOUT TIME ZONE,
    revoked      BOOLEAN NOT NULL DEFAULT FALSE,
    user_id      UUID NOT NULL,
    CONSTRAINT pk_tb_refresh_tokens PRIMARY KEY (id)
);

ALTER TABLE tb_refresh_tokens
    ADD CONSTRAINT FK_TB_REFRESH_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES tb_users (id);