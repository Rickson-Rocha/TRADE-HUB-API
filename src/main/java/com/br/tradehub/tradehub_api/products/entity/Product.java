package com.br.tradehub.tradehub_api.products.entity;


import com.br.tradehub.tradehub_api.auth.entity.User;
import com.br.tradehub.tradehub_api.products.enums.ProductCategory;
import com.br.tradehub.tradehub_api.products.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "tb_products")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "seller_id")
    private User seller;

    private String description;

    private String name;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Enumerated(EnumType.STRING)
    private ProductCategory categoryProduct;

    private LocalDateTime createdAt;
}
