package com.br.tradehub.tradehub_api.products.service;


import com.br.tradehub.tradehub_api.auth.entity.User;
import com.br.tradehub.tradehub_api.auth.repository.UserRepository;
import com.br.tradehub.tradehub_api.products.dto.ProductRequest;
import com.br.tradehub.tradehub_api.products.dto.ProductResponse;
import com.br.tradehub.tradehub_api.products.entity.Product;
import com.br.tradehub.tradehub_api.products.enums.ProductCategory;
import com.br.tradehub.tradehub_api.products.enums.ProductStatus;
import com.br.tradehub.tradehub_api.products.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ProductServiceImpl(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse create(ProductRequest product) {
        User seller  = this.getAuthenticatedUser();
        Product newProduct = new Product();
        newProduct.setName(product.name());
        newProduct.setPrice(product.price());
        newProduct.setDescription(product.description());
        newProduct.setStatus(ProductStatus.ACTIVE);
        newProduct.setSeller(seller);
        newProduct.setCategoryProduct(product.categoryProduct());
        productRepository.save(newProduct);
        return new ProductResponse(newProduct.getId(),
                newProduct.getName(), newProduct.getDescription(),
                 newProduct.getPrice(),newProduct.getStatus(), newProduct.getCategoryProduct()
                ,newProduct.getSeller().getUsername(),newProduct.getSeller().getId());
    }

    @Override
    public ProductResponse findById(UUID id) {
        Optional<Product> savedProduct = productRepository.findByIdWithSeller(id);
        if(savedProduct.isEmpty()){
            throw new IllegalArgumentException("Product not found");
        }
        Product product = savedProduct.get();
        return new ProductResponse(product.getId(),
                product.getName(), product.getDescription(),
                product.getPrice(),product.getStatus(), product.getCategoryProduct()
                ,product.getSeller().getUsername(),product.getSeller().getId());
    }

    @Override
    public void updateProduct(ProductRequest productRequest, UUID id) {
        Product savedProduct =
                productRepository.findByIdWithSeller(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if(!savedProduct.getSeller().getId().equals(this.getAuthenticatedUser().getId())){
            throw new IllegalArgumentException("User not authorized");
        }
        savedProduct.setName(productRequest.name());
        savedProduct.setPrice(productRequest.price());
        savedProduct.setDescription(productRequest.description());
        savedProduct.setCategoryProduct(productRequest.categoryProduct());
        productRepository.save(savedProduct);

    }

    @Override
    public void deleteProduct(UUID id) {
        Product savedProduct =
                productRepository.findByIdWithSeller(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if(!savedProduct.getSeller().getId().equals(this.getAuthenticatedUser().getId())){
            throw new IllegalArgumentException("User not authorized");
        }
        savedProduct.setStatus(ProductStatus.INACTIVE);
        productRepository.save(savedProduct);

    }

    @Override
    public Page<ProductResponse> findAll(Pageable pageable, String name, BigDecimal minPrice, BigDecimal maxPrice, ProductCategory category) {
        Page<Product> products = productRepository.findAllWithFilters(name, minPrice, maxPrice, category, pageable);

        return  products.map(product -> new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStatus(),
                product.getCategoryProduct(),
                product.getSeller().getUsername(),
                product.getSeller().getId()
        ));
    }


    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
