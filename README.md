# Система бронирований отелей

## Описание проекта 
Данный проект представляет собой реализацию backend-системы для бронирования отелей на основе микросервисной архитектуры с использованием Spring Boot и Spring Cloud. 
Система разработана в соответствии с принципами предметно-ориентированного проектирования и включает в себя следующие микросервисы:

- `Eureka Server` – сервис регистрации и обнаружения.

- `API Gateway (Spring Cloud Gateway)` – единая точка входа, маршрутизация запросов, сквозная аутентификация JWT.

- `Booking Service` – управление пользователями, аутентификация, создание бронирований, координация саги.

- `Hotel Management Service` – управление отелями и номерами, проверка доступности, временная блокировка номеров, статистика загрузки.

Система поддерживает две роли: USER (обычный пользователь) и ADMIN (администратор)

## Технологический стек
- Java 21
- Spring Boot 4.0.2
- Spring Cloud (Eureka, Gateway, OpenFeign)
- Spring Security + OAuth2 Resource Server (JWT)
- Spring Data JPA / Hibernate
- H2 Database (in-memory)
- Resilience4j (retry, time limiter)
- Springdoc OpenAPI (Swagger-UI)
- Lombok
- Maven

## Запуск сервисов
```
cd eureka
mvn spring-boot:run
```
eureka будет доступна на порту 8761
```
cd hotel-managment
mvn spring-boot:run
```
hotel-managment будет доступна на порту 8081
```
cd booking
mvn spring-boot:run
```
booking будет доступна на порту 8082
```
cd gateway
mvn spring-boot:run
```
gateway будет доступен на порту 8080


## Основной функционал

**Для обычных пользователей:**
- Регистрация и вход
- Просмотр отелей и номеров
- Бронирование номеров
- Просмотр своих бронирований
- Отмена бронирования

**Для админов:**
- Управление отелями/номерами

## Документация
Для hotel-managment и booking доступна документация в swagger-ui
По адрессу localhost:{port}/swagger-ui/index.html

