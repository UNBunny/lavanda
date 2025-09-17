# Order Management Service

Микросервис управления заказами для ERP системы цветочной мастерской LAVANDA.

## Описание

Order Management Service предназначен для полного управления жизненным циклом заказов в флористическом бизнесе. Сервис поддерживает создание заказов, отслеживание статусов, управление позициями заказов, назначение флористов и предоставляет аналитику по заказам.

## Основные возможности

- 📝 **Управление заказами**: создание, редактирование, удаление заказов
- 📊 **Отслеживание статусов**: полный жизненный цикл заказа от создания до доставки
- 🌸 **Флористическая специфика**: учет цветов поштучно, материалов по метрам
- 👨‍🎨 **Назначение флористов**: распределение заказов между сотрудниками
- 📈 **Аналитика**: статистика продаж, популярные товары, производительность
- 🔍 **Поиск и фильтрация**: гибкий поиск заказов по различным критериям
- 💰 **Управление платежами**: отслеживание статусов оплаты и расчетов

## Архитектура

```
order-service/
├── src/main/java/com/omstu/lavanda/order/
│   ├── config/          # Конфигурация Spring Boot и OpenAPI
│   ├── controller/      # REST контроллеры
│   ├── dto/            # Data Transfer Objects
│   ├── exception/      # Обработка исключений
│   ├── mapper/         # Мапперы Entity <-> DTO
│   ├── model/          # JPA сущности
│   ├── repository/     # Репозитории данных
│   └── service/        # Бизнес-логика
└── src/main/resources/
    ├── db/changelog/   # Миграции Liquibase
    └── application.yml # Конфигурация приложения
```

## Модель данных

### Order (Заказ)
- Основная информация о заказе
- Данные клиента и доставки
- Статус заказа и временные метки
- Назначенный флорист
- Финансовая информация

### OrderItem (Позиция заказа)
- Товар и его характеристики
- Количество и цены
- Скидки и примечания
- Связь с букетами (для композиций)

### OrderStatus (Статус заказа)
- `NEW` - Новый заказ
- `CONFIRMED` - Подтвержден
- `IN_PROGRESS` - В работе
- `READY` - Готов к выдаче
- `OUT_FOR_DELIVERY` - В доставке
- `DELIVERED` - Доставлен
- `CANCELLED` - Отменен
- `RETURNED` - Возвращен

## API Endpoints

### Основные операции
- `POST /api/orders` - Создание заказа
- `GET /api/orders/{id}` - Получение заказа по ID
- `PUT /api/orders/{id}` - Обновление заказа
- `DELETE /api/orders/{id}` - Удаление заказа

### Управление статусами
- `PUT /api/orders/{id}/status` - Изменение статуса
- `PUT /api/orders/{id}/assign/{floristId}` - Назначение флориста
- `PUT /api/orders/{id}/cancel` - Отмена заказа

### Поиск и фильтрация
- `GET /api/orders/search` - Поиск заказов
- `GET /api/orders/by-status/{status}` - Заказы по статусу
- `GET /api/orders/by-customer` - Заказы клиента
- `GET /api/orders/by-florist/{floristId}` - Заказы флориста

### Аналитика
- `GET /api/orders/statistics` - Общая статистика
- `PUT /api/orders/{id}/recalculate` - Пересчет суммы

## Технологии

- **Java 21** - Основной язык программирования
- **Spring Boot 3.x** - Фреймворк приложения
- **Spring Data JPA** - Работа с базой данных
- **PostgreSQL** - База данных
- **Liquibase** - Миграции БД
- **Lombok** - Упрощение кода
- **OpenAPI/Swagger** - Документация API
- **Maven** - Сборка проекта

## Конфигурация

### База данных
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/lavanda_orders
    username: ${DB_USERNAME:lavanda_user}
    password: ${DB_PASSWORD:lavanda_password}
```

### Порт и контекст
```yaml
server:
  port: 8081
  servlet:
    context-path: /order-service
```

## Запуск

### Локальный запуск
```bash
# Сборка проекта
mvn clean install

# Запуск сервиса
mvn spring-boot:run
```

### Docker
```bash
# Сборка образа
docker build -t lavanda/order-service .

# Запуск контейнера
docker run -p 8081:8081 lavanda/order-service
```

## Документация API

После запуска сервиса документация доступна по адресу:
- Swagger UI: http://localhost:8081/order-service/swagger-ui.html
- OpenAPI JSON: http://localhost:8081/order-service/v3/api-docs

## Мониторинг

Actuator endpoints доступны по адресу:
- Health: http://localhost:8081/order-service/actuator/health
- Metrics: http://localhost:8081/order-service/actuator/metrics
- Info: http://localhost:8081/order-service/actuator/info

## Интеграция

Order Management Service интегрируется с другими микросервисами LAVANDA ERP:
- **Inventory Service** - проверка наличия товаров
- **Finance Service** - обработка платежей
- **Notification Service** - уведомления клиентов
- **Employee Service** - управление флористами

## Разработка

### Добавление новых функций
1. Создайте новую ветку от `main`
2. Реализуйте функциональность
3. Добавьте тесты
4. Обновите документацию
5. Создайте Pull Request

### Миграции БД
Новые миграции добавляются в `src/main/resources/db/changelog/` и регистрируются в `db.changelog-master.xml`.

## Лицензия

MIT License - см. файл LICENSE для деталей.
