package com.br.tradehub.tradehub_api.products.dto;

import com.br.tradehub.tradehub_api.products.enums.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record ProductRequest(

        @NotBlank
        String name,
        @NotBlank
        String description,

        @NotNull
        BigDecimal price,

        @NotNull
        ProductCategory categoryProduct

) {
}
