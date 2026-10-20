package org.example.service;

import org.example.model.Product;
import org.example.model.ProductType;
import org.example.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


public class ProductServiceTest {
    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);

    private final R2dbcEntityTemplate template = Mockito.mock(R2dbcEntityTemplate.class);

    private final ProductCache productCache = Mockito.mock(ProductCache.class);

    private final ProductService productService = new ProductService(productRepository, template, productCache);

    @Test
    void shouldReturnProductWhenExists(){
        UUID id = UUID.randomUUID();
        Product mockProduct = new Product(id, ProductType.LOAN, "Вклад", "Накопительный вклад", LocalDate.of(2026, 1, 15),
                null);

        when(productRepository.findById(id)).thenReturn(Mono.just(mockProduct));

        StepVerifier.create(productService.getById(id))
                .assertNext(actual -> assertThat(actual.id()).isEqualTo(id))
                .verifyComplete();
        verify(productRepository).findById(id);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldReturnErrorWhenUserNotFound(){
        UUID id = UUID.randomUUID();

        when(productRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(productService.getById(id))
                .expectError(NoSuchElementException.class)
                .verify();
    }

    @Test
    void shouldDeleteProductWhenExists() {
        UUID id = UUID.randomUUID();

        Product product = new Product(id, ProductType.CARD, "Дебетовая карта", "Карточный продукт", LocalDate.of(2026, 2, 1),
                null);

        when(productRepository.findById(id)).thenReturn(Mono.just(product));
        when(productRepository.delete(product)).thenReturn(Mono.empty());

        StepVerifier.create(productService.delete(id)).verifyComplete();

        verify(productRepository).findById(id);
        verify(productRepository).delete(product);
        verify(productCache).delete(id);
        verifyNoMoreInteractions(productRepository, productCache);
    }
}
