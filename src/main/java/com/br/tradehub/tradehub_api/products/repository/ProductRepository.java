package com.br.tradehub.tradehub_api.products.repository;

import com.br.tradehub.tradehub_api.products.entity.Product;
import com.br.tradehub.tradehub_api.products.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p JOIN FETCH p.seller WHERE p.id = :id")
    Optional<Product> findByIdWithSeller(@Param("id") UUID id);

    @Query("SELECT p FROM Product p JOIN FETCH p.seller WHERE " +
            "p.status = 'ACTIVE' AND " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:category IS NULL OR p.categoryProduct = :category)")
    Page<Product> findAllWithFilters(@Param("name") String name,
                                     @Param("minPrice") BigDecimal minPrice,
                                     @Param("maxPrice") BigDecimal maxPrice,
                                     @Param("category") ProductCategory category,
                                     Pageable pageable);
}
