package org.example.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.example.model.Product;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class ProductCache {
    private final Cache<UUID, Product> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofSeconds(30))
            .maximumSize(10_000)
            .build();

    public void put(Product product){
        cache.put(product.id(), product);
    }

    public  Product get(UUID id){
         return cache.getIfPresent(id);
    }

    public  void delete(UUID id){
        cache.invalidate(id);
    }
}
