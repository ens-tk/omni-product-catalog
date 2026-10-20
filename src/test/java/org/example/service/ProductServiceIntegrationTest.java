package org.example.service;

import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.example.dto.CreateProductRequest;
import org.example.dto.UpdateProductRequest;
import org.example.model.Product;
import org.example.model.ProductType;
import org.example.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class ProductServiceIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        // Конвертируем JDBC URL в R2DBC URL для реактивного драйвера
        String r2dbcUrl = String.format("r2dbc:postgresql://%s:%d/%s",
                postgres.getHost(),
                postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgres.getDatabaseName());

        registry.add("spring.r2dbc.url", () -> r2dbcUrl);
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);

        // Liquibase использует JDBC, а не R2DBC
        registry.add("spring.liquibase.url", postgres::getJdbcUrl);
        registry.add("spring.liquibase.user", postgres::getUsername);
        registry.add("spring.liquibase.password", postgres::getPassword);
    }

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp(){
        productRepository.deleteAll().block();
    }

    @Test
    void shouldCreateProductAndReadItFromDatabase(){
        CreateProductRequest request = new CreateProductRequest(ProductType.LOAN, "Вклад", "Накопительный вклад", LocalDate.of(2026, 1, 15),
                null);


        StepVerifier.create(productService.create(request)
                .flatMap(product -> productService.getById(product.id())))
                .assertNext(product -> {
                    assertThat(product.id()).isNotNull();
                    assertThat(product.type()).isEqualTo(request.type());
                    assertThat(product.name()).isEqualTo(request.name());
                    assertThat(product.description()).isEqualTo(request.description());
                    assertThat(product.openDate()).isEqualTo(request.openDate());
                    assertThat(product.closeDate()).isEqualTo(request.closeDate());
                })
                .verifyComplete();

    }

    @Test
    void shouldUpdateProductInDatabase() {
        CreateProductRequest createRequest = new CreateProductRequest(ProductType.CARD, "Дебетовая карта", "Старая версия", LocalDate.of(2026, 2, 1),
                        null);

        UpdateProductRequest updateRequest = new UpdateProductRequest(ProductType.CARD, "Премиальная карта", "Обновлённое описание", LocalDate.of(2026, 2, 1),
                        null);

        StepVerifier.create(productService.create(createRequest)
                                .flatMap(created -> productService.update(created.id(), updateRequest))
                                .flatMap(updated -> productService.getById(updated.id())))
                .assertNext(product -> {
                    assertThat(product.name()).isEqualTo(updateRequest.name());
                    assertThat(product.description()).isEqualTo(updateRequest.description());
                    assertThat(product.type()).isEqualTo(updateRequest.type());
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        StepVerifier.create(productRepository.findById(UUID.randomUUID()))
                .expectNextCount(0)
                .verifyComplete();
    }
}
