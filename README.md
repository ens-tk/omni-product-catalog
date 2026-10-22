# omni-product-catalog

## Описание
REST API для управления каталогом продуктов.

## Назначение
Создание, получение, обновление и удаление продуктов.

## Стек
- Java
- Spring Boot, Spring WebFlux
- Spring Data R2DBC
- PostgreSQL
- Liquibase
- Caffeine
- Spring Boot Actuator
- Docker Compose
- StepVerifier, Assertj, Mockito, Testcontainers,

## Локальный запуск

1. Скопируйте `.env.example` в `.env`.
2. Укажите необходимые значения переменных окружения.
3. Запустите базу данных: `docker compose up -d`.
4. Запустите приложение.
