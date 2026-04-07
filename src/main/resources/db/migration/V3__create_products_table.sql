CREATE TABLE tb_products
(
    id               UUID NOT NULL,
    seller_id        UUID NOT NULL,
    description      VARCHAR(255) NOT NULL ,
    name             VARCHAR(255) NOT NULL ,
    price            DECIMAL NOT NULL ,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    category_product VARCHAR(255) NOT NULL ,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_tb_products PRIMARY KEY (id)
);

ALTER TABLE tb_products
    ADD CONSTRAINT FK_TB_PRODUCTS_ON_SELLER FOREIGN KEY (seller_id) REFERENCES tb_users (id);