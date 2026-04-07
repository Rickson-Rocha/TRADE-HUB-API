package com.br.tradehub.tradehub_api.products.service;

import com.br.tradehub.tradehub_api.products.dto.ProductRequest;
import com.br.tradehub.tradehub_api.products.dto.ProductResponse;
import com.br.tradehub.tradehub_api.products.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductService {

    ProductResponse create (ProductRequest product);
    ProductResponse findById (UUID id);
    void updateProduct(ProductRequest productRequest, UUID id);
    void deleteProduct (UUID id);
    Page<ProductResponse> findAll(Pageable pageable, String name, BigDecimal minPrice, BigDecimal maxPrice, ProductCategory category);
}
