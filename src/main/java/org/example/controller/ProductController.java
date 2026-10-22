package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.CreateProductRequest;
import org.example.dto.UpdateProductRequest;
import org.example.exception.ProductTimeoutException;
import org.example.model.Product;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    private final ProductService productService;

    @Value("${product.timeout.create-millis:10000}")
    private  int createTimeout;

    @Value("${product.timeout.update-millis:10000}")
    private  int updateTimeout;

    @Value("${product.timeout.get-millis:10000}")
    private  int getTimeout;

    @Value("${product.timeout.delete-millis:10000}")
    private  int deleteTimeout;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Mono<Product> create(
             @Valid @RequestBody CreateProductRequest request
    ) {
        return productService.create(request)
                .timeout(Duration.ofMillis(createTimeout))
                .onErrorMap(TimeoutException.class, ex -> new ProductTimeoutException("Превышено время ожидания создания продукта"));
    }

    @GetMapping("/{id}")
    public Mono<Product> getById(@PathVariable UUID id){
        return productService.getById(id)
                .timeout(Duration.ofMillis(getTimeout))
                .onErrorMap(TimeoutException.class, ex -> new ProductTimeoutException("Превышено время ожидания получения продукта"));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable UUID id){
        return productService.delete(id)
                .timeout(Duration.ofMillis(deleteTimeout))
                .onErrorMap(TimeoutException.class, ex -> new ProductTimeoutException("Превышено время ожидания удаления продукта"));
    }

    @PostMapping("/{id}")
    public Mono<Product> update(@PathVariable UUID id, @Valid @RequestBody UpdateProductRequest request){
        return productService.update(id, request)
                .timeout(Duration.ofMillis(updateTimeout))
                .onErrorMap(TimeoutException.class, ex -> new ProductTimeoutException("Превышено время ожидания обновления продукта"));
    }
}
