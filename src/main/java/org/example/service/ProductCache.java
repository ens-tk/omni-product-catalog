package org.example.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.example.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class ProductCache {
    @Value("${product.cache.ttl-millis:30000}") long ttlMillis;
    @Value("${product.cache.max-size:10000}") long maxSize;

    private final Cache<UUID, Product> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMillis(ttlMillis))
            .maximumSize(maxSize)
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
