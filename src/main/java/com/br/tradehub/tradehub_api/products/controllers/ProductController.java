package com.br.tradehub.tradehub_api.products.controllers;


import com.br.tradehub.tradehub_api.products.dto.ProductRequest;
import com.br.tradehub.tradehub_api.products.dto.ProductResponse;
import com.br.tradehub.tradehub_api.products.enums.ProductCategory;
import com.br.tradehub.tradehub_api.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/trade-hub/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public EntityModel<ProductResponse> getProductById(@PathVariable("id") UUID id){
        ProductResponse productResponse = productService.findById(id);
        return  EntityModel.of(
                productResponse,
                linkTo(methodOn(ProductController.class).getProductById(id)).withSelfRel(),
                linkTo(methodOn(ProductController.class).findAll(null,null, null, null, null, null)).withRel("products")
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<EntityModel<ProductResponse>> create(@RequestBody @Valid ProductRequest product) {
        ProductResponse response = productService.create(product);
        EntityModel<ProductResponse> model = EntityModel.of(
                response,
                linkTo(methodOn(ProductController.class).getProductById(response.id())).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> updateProduct(@RequestBody @Valid ProductRequest productRequest, @PathVariable("id") UUID id){
        productService.updateProduct(productRequest, id);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") UUID id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public PagedModel<EntityModel<ProductResponse>> findAll(
            Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) ProductCategory category,
            PagedResourcesAssembler<ProductResponse> assembler) {

        Page<ProductResponse> page = productService.findAll(pageable, name, minPrice, maxPrice, category);
        return assembler.toModel(page);
    }
}
