CREATE TABLE tb_users
(
    id         UUID NOT NULL,
    username   VARCHAR(255)NOT NULL,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) UNIQUE,
    role       VARCHAR(255) NOT NULL DEFAULT 'CUSTOMER',
    status     VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_tb_users PRIMARY KEY (id)
);