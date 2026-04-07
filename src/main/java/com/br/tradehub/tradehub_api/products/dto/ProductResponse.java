package com.br.tradehub.tradehub_api.products.dto;

import com.br.tradehub.tradehub_api.products.enums.ProductCategory;
import com.br.tradehub.tradehub_api.products.enums.ProductStatus;


import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(

        UUID id,

        String name,

        String description,


        BigDecimal price,

        ProductStatus status,

        ProductCategory categoryProduct,

        String sellerName,

        UUID sellerId


) {
}
