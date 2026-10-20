package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.CreateProductRequest;
import org.example.dto.UpdateProductRequest;
import org.example.model.Product;
import org.example.service.ProductService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Mono<Product> create(
             @RequestBody CreateProductRequest request
    ) {
        return productService.create(request);
    }

    @GetMapping("/{id}")
    public Mono<Product> getById(@PathVariable UUID id){
        return productService.getById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable UUID id){
        return productService.delete(id);
    }

    @PostMapping("/{id}")
    public Mono<Product> update(@PathVariable UUID id, @RequestBody UpdateProductRequest request){
        return productService.update(id, request);
    }
}
