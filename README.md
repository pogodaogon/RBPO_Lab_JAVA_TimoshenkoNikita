# Система управления рестораном и бронированием столиков

## Описание проекта

Spring Boot приложение для комплексного управления рестораном, включая бронирование столиков, управление клиентской базой, меню и информацией о ресторанах. Приложение предоставляет REST API для всех операций и использует in-memory хранилище данных.

## Функциональные возможности

### Управление ресторанами
- Создание, чтение, обновление и удаление ресторанов
- Хранение информации: название, адрес, телефон, часы работы, описание

### Управление столиками
- Создание столиков с указанием вместимости и местоположения
- Отслеживание доступности столиков
- Фильтрация столиков по ресторану

### Управление клиентами
- Регистрация новых клиентов
- Обновление клиентской информации
- Полное управление клиентской базой

### Управление меню
- Создание и редактирование пунктов меню
- Категоризация (закуски, основные блюда, десерты, напитки)
- Управление ценами и доступностью

### Система бронирования
- Бронирование столиков с проверкой доступности
- Проверка пересечений по времени
- Поддержка специальных запросов
- Поиск доступных столиков по дате, времени и количеству гостей
- Управление статусами бронирований (PENDING, CONFIRMED, CANCELLED, COMPLETED)

## Технологический стек

- **Язык программирования**: Java 17+
- **Фреймворк**: Spring Boot 3.x
- **Веб-слой**: Spring Web MVC (REST API)
- **Управление зависимостями**: Maven
- **Утилиты**: Lombok для сокращения boilerplate-кода
- **Тестирование**: Spring Boot Test, JUnit

## Архитектура проекта

Приложение следует многослойной архитектуре:

1. **Модель (Model)**: Сущности предметной области
2. **Репозиторий (Repository)**: Слой доступа к данным (in-memory HashMap)
3. **Сервис (Service)**: Бизнес-логика и валидация
4. **Контроллер (Controller)**: REST API endpoints

## Структура пакетов
ru.mfa
├── MavenDemoApplication.java # Точка входа приложения
├── model # Модели данных
│ ├── Customer.java
│ ├── MenuItem.java
│ ├── Reservation.java
│ ├── Restaurant.java
│ └── Table.java
├── repository # Репозитории (хранилища данных)
│ ├── CustomerRepository.java
│ ├── MenuItemRepository.java
│ ├── ReservationRepository.java
│ ├── RestaurantRepository.java
│ └── TableRepository.java
├── service # Сервисный слой
│ ├── CustomerService.java
│ ├── MenuItemService.java
│ ├── ReservationService.java
│ ├── RestaurantService.java
│ └── TableService.java
└── web # Контроллеры (REST API)
├── CustomerController.java
├── HelloController.java
├── MenuItemController.java
├── ReservationController.java
├── RestaurantController.java
└── TableController.java



## Модели данных

### Customer (Клиент)
```java
private Long id;
private String name;
private String email;
private String phone;
private String notes;
Restaurant (Ресторан)
java
private Long id;
private String name;
private String address;
private String phone;
private String openingHours;
private String description;
Table (Столик)
java
private Long id;
private Long restaurantId;
private String tableNumber;
private Integer capacity;
private String location; // "у окна", "в зале", "на террасе", "VIP"
private Boolean isAvailable;
MenuItem (Пункт меню)
java
private Long id;
private Long restaurantId;
private String name;
private String description;
private String category; // "закуски", "основные блюда", "десерты", "напитки"
private BigDecimal price;
private Boolean isAvailable;
Reservation (Бронирование)
java
private Long id;
private Long customerId;
private Long tableId;
private LocalDateTime reservationTime;
private Integer durationHours;
private Integer numberOfGuests;
private String status; // "PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"
private String specialRequests;
private LocalDateTime createdAt;
API Endpoints
Базовые endpoints
GET /api/hello - Тестовое приветствие

GET /api/greet/{name} - Приветствие по имени

GET /api/ping - Проверка работоспособности API

Клиенты (/api/customers)
POST /api/customers - Создать нового клиента

GET /api/customers - Получить всех клиентов

GET /api/customers/{id} - Получить клиента по ID

PUT /api/customers/{id} - Обновить информацию о клиенте

DELETE /api/customers/{id} - Удалить клиента

Рестораны (/api/restaurants)
POST /api/restaurants - Создать новый ресторан

GET /api/restaurants - Получить все рестораны

GET /api/restaurants/{id} - Получить ресторан по ID

PUT /api/restaurants/{id} - Обновить информацию о ресторане

DELETE /api/restaurants/{id} - Удалить ресторан

Столики (/api/tables)
POST /api/tables - Создать новый столик

GET /api/tables - Получить все столики

GET /api/tables/{id} - Получить столик по ID

GET /api/tables/restaurant/{restaurantId} - Получить столики ресторана

PUT /api/tables/{id} - Обновить информацию о столике

DELETE /api/tables/{id} - Удалить столик

Пункты меню (/api/menu-items)
POST /api/menu-items - Создать новый пункт меню

GET /api/menu-items - Получить все пункты меню

GET /api/menu-items/{id} - Получить пункт меню по ID

GET /api/menu-items/restaurant/{restaurantId} - Получить меню ресторана

PUT /api/menu-items/{id} - Обновить пункт меню

DELETE /api/menu-items/{id} - Удалить пункт меню

Бронирования (/api/reservations)
POST /api/reservations - Создать новое бронирование

GET /api/reservations - Получить все бронирования

GET /api/reservations/{id} - Получить бронирование по ID

GET /api/reservations/customer/{customerId} - Получить бронирования клиента

GET /api/reservations/table/{tableId} - Получить бронирования столика

GET /api/reservations/available - Найти доступные столики

Параметры: startTime, durationHours, guests

PUT /api/reservations/{id} - Обновить бронирование

PUT /api/reservations/{id}/cancel - Отменить бронирование

DELETE /api/reservations/{id} - Удалить бронирование

Логика бронирования
При создании бронирования выполняются следующие проверки:

Существование клиента - проверка, что клиент существует в системе

Существование столика - проверка, что столик существует

Доступность столика - проверка флага isAvailable

Вместимость - проверка, что количество гостей не превышает вместимость столика

Пересечение по времени - проверка, что столик свободен в запрашиваемое время

Установка и запуск
Предварительные требования
Java Development Kit (JDK) 17 или выше

Apache Maven 3.6 или выше

Любая IDE (IntelliJ IDEA, Eclipse, VS Code) или командная строка

Сборка проекта
Клонируйте репозиторий или скопируйте исходный код

Перейдите в корневую директорию проекта

Выполните сборку:


mvn clean package
Запуск приложения
Способ 1: Через Maven
bash
mvn spring-boot:run
Способ 2: Через собранный JAR-файл
bash
java -jar target/RBPO_Lab_JAVA-1.0.0.jar
Порт приложения
По умолчанию приложение запускается на порту 8080.

Примеры использования API
Создание клиента
bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Алексей Петров",
    "email": "alexey@example.com",
    "phone": "+79161234567",
    "notes": "Предпочитает столики у окна"
  }'
Создание ресторана
bash
curl -X POST http://localhost:8080/api/restaurants \
  -H "Content-Type: application/json" \
  -d '{
    "name": "La Bella Italia",
    "address": "ул. Пушкинская, д. 15",
    "phone": "+74951234567",
    "openingHours": "12:00-23:00",
    "description": "Ресторан итальянской кухни с авторскими блюдами"
  }'
Создание столика
bash
curl -X POST http://localhost:8080/api/tables \
  -H "Content-Type: application/json" \
  -d '{
    "restaurantId": 1,
    "tableNumber": "A1",
    "capacity": 4,
    "location": "у окна",
    "isAvailable": true
  }'
Создание бронирования

curl -X POST http://localhost:8080/api/reservations \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "tableId": 1,
    "reservationTime": "2024-12-25T19:00:00",
    "durationHours": 2,
    "numberOfGuests": 3,
    "specialRequests": "Просим приготовить торт на день рождения"
  }'
Поиск доступных столиков

curl "http://localhost:8080/api/reservations/available?startTime=2024-12-25T20:00:00&durationHours=2&guests=4"
Тестирование
Запуск тестов

mvn test
Интеграционные тесты
Проект включает интеграционные тесты для проверки работы REST endpoints:

HelloControllerTest - проверяет работу базовых endpoints

Расширение тестового покрытия
Рекомендуется добавить:

Unit-тесты для сервисов

Интеграционные тесты для всех контроллеров

Тесты на граничные случаи и ошибки

Особенности реализации
Хранение данных
Используется in-memory хранилище на основе HashMap

Данные сохраняются только во время работы приложения

Автоматическая генерация ID для новых записей

Валидация
Проверка всех обязательных условий при бронировании

Обработка конфликтов по времени

Проверка ссылочной целостности (существование клиентов, столиков)

Обработка ошибок
Возврат соответствующих HTTP статусов

Информативные сообщения об ошибках

Обработка исключительных ситуаций

Расширение функциональности
Планируемые улучшения
Хранение данных

Добавление базы данных (PostgreSQL, MySQL)

Миграции для схемы БД

Безопасность

Аутентификация и авторизация (Spring Security)

JWT-токены для доступа к API

Роли пользователей (администратор, менеджер, клиент)

Документация API

Добавление Swagger/OpenAPI документации

Интерактивная документация API

Дополнительные функции

Система уведомлений (email/SMS)

История посещений клиентов

Система лояльности и скидок

Генерация отчетов

Пагинация и фильтрация для списков

Фронтенд

Веб-интерфейс на React/Vue.js

Мобильное приложение

Тестирование

Полное покрытие unit-тестами

Интеграционные тесты

Тесты производительности
