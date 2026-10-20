package org.example.service;

import org.example.dto.CreateProductRequest;
import org.example.dto.CreateProductResponse;
import org.example.dto.UpdateProductRequest;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final R2dbcEntityTemplate template;
    private final ProductCache productCache;

    public ProductService(ProductRepository productRepository, R2dbcEntityTemplate template, ProductCache productCache) {
        this.productRepository = productRepository;
        this.template = template;
        this.productCache = productCache;
    }

    public Mono<Product> create(CreateProductRequest request){
        if (request.closeDate() != null && request.openDate().isAfter(request.closeDate())){
            return Mono.error(new IllegalArgumentException("Дата открытия должна быть до даты окончания"));
        }
        Product product = new Product(UUID.randomUUID(),
                request.type(),
                request.name(),
                request.description(),
                request.openDate(),
                request.closeDate());

        return template.insert(Product.class).using(product).doOnSuccess(productCache::put);
    }

    public Mono<Product> update(UUID id, UpdateProductRequest request){
        if (request.closeDate() != null && request.openDate().isAfter(request.closeDate())){
            return Mono.error(new IllegalArgumentException("Дата открытия должна быть до даты окончания"));
        }

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Product not found with id" + id)))
                .flatMap(p -> {
                    Product product = new Product(id, request.type(), request.name(), request.description(), request.openDate(), request.closeDate());
                    return productRepository.save(product);
                })
                .doOnSuccess(productCache::put);
    }

    public Mono<Product> getById(UUID id){
        Product product = productCache.get(id);
        if (product!=null)
            return Mono.just(product);
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Product not found with id" + id)))
                .doOnSuccess(p -> productCache.put(p));
    }

    public Mono<Void> delete(UUID id){
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Product not found with id" + id)))
                .flatMap(product -> productRepository.delete(product))
                .doOnSuccess(product -> productCache.delete(id));
    }

}
