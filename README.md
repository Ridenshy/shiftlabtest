# SHIFT LAB CRM SYSTEM

# О проекте

ShiftCRM - это REST API система для управления продавцами, транзакциями и аналитикой продаж.
Система содержит все необходимые методы для управления данными. Методы разделены на отдельные сервисы,
Получение данных с потенциально большим объемом производится при помощи пагинации.
Все данные валидируются, ошибки обрабатываются глобальным обработчиком и возвращают соответствующий ответ клиенту.

# Функционал
- Управление продавцами (/sellers):
    - Получение списка всех продавцов
    - Получение информации о продавце по id
    - Создание продавца
    - Обновление продавца по id
    - Удаление продавца по id


- Управление транзакциями (/transactions):
    - Получение списка всех транзакций
    - Получение списка всех транзакций для конкретного продавца
    - Получение информации о транзакции по id
    - Создание транзакции


- Аналитика (/analytics)
    - Получение самого продуктивного продавца в рамках указанного типа периода
    - Получение списка продавцов, чья сумма всех транзакций за указанный период меньше указанной суммы
    - Расчет и получение наилучшего периода времени для конкретного продавца по его id

# Тестирование

Тестирование покрывает все API контроллеры, а также сервисный слой работающий с базой данных
Всего реализованно 88 тестов
- Тестирование сервисов происходит при помощи Testcontainers, реализовано тестирование удачных и не удачных сценариев, проверка обработки ошибок.
- Тестирование API происходит при помощи Mockito. Реализованы успешные и не успешные сценарии, проверка статусов ответа.

# Технологии и Зависимости

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Gradle 8.12.1](https://gradle.org/install/)


- [Spring Boot 3.5.5](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter/3.5.5)
- [Spring Boot Starter Web 3.5.5](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web/3.5.5)
- [Spring Boot Starter Test 3.5.5](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test/3.5.5)
- [Spring Boot Starter Data JPA 3.5.5](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa/3.5.5)
- [Spring Boot Starter Validation 3.5.5](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation/3.5.5)
- [Spring Boot Testcontainers 3.5.5](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-testcontainers/3.5.5)
- [Liquibase](https://mvnrepository.com/artifact/org.liquibase/liquibase-core)
- [Project Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)
- [MapStruct](https://mvnrepository.com/artifact/org.mapstruct/mapstruct/1.5.5.Final)
- [SpringDoc OpenAPI Starter WebMVC UI](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui/2.8.5)
- [PostgreSQL JDBC Driver](https://mvnrepository.com/artifact/org.postgresql/postgresql)

# Сборка и запуск
1. Сборка и запуск проекта jar
   - Запуск контейнера базы данных <pre>docker-compose up -d postgres</pre><br>
   - Компиляция программы <pre>./gradlew clean build</pre><br>
   - Запуск программы <pre>java -jar build/libs/ShiftCRM-0.0.1.jar</pre><br>
2. Сборка и запуск проекта в контейнере docker
   - Компиляция приложения <pre>./gradlew clean build</pre><br>
   - Запуск контейнеров БД и приложения <pre>docker-compose up -d</pre><br>

# Swagger

В проекте используется SpringDoc OpenAPI Starter WebMVC UI для реализации документирования и 
интегрированного теста API приложения, для просмотра перейдите на эндпоинт:
http://localhost:8080/swagger-ui/index.html#/


# Использование API

**Endpoint**
``GET /sellers``<br>
**Response**<br>
```json
{
  "content": [
    {
      "id": 1,
      "name": "name",
      "contactInfo": "contact",
      "registrationDate": "2025-25-12T12:12:12.121212"
    },
    {
      "id": 2,
      "name": "name2",
      "contactInfo": "contact2",
      "registrationDate": "2025-25-12T12:12:12.121212"
    }
  ],
  "page": {
    "size": 50,
    "number": 0,
    "totalElements": 2,
    "totalPages": 1
  }
}
```

**Endpoint**
``GET /sellers/2``<br>
**Response**<br>
```json
{
  "id": 2,
  "name": "name2",
  "contactInfo": "contact2",
  "registrationDate": "2025-25-12T12:12:12.121212"
}
```

**Endpoint**
``POST /sellers``<br>
**Body**<br>
```json
{
  "name": "Евгений",
  "contactInfo": "e@gmail.ru"
}
```
**Response**
``201 OK``
```json
{
  "id": 2,
  "name": "name2",
  "contactInfo": "contact2",
  "registrationDate": "2025-25-12T12:12:12.121212"
}
```

**Endpoint**
``PATCH /sellers/1``<br>
**Body**<br>
```json
{
  "contactInfo": "eee@gmail.ru"
}
```
**Response**
``200 OK``
```json
{
  "id": 2,
  "name": "name2",
  "contactInfo": "contact2",
  "registrationDate": "2025-25-12T12:12:12.121212"
}
```
**Endpoint**
``DELETE /sellers/1``<br>
**Response**
``204 OK``

**Endpoint**
``GET /transactions``<br>
**Response**<br>
```json
{
  "content": [
    {
      "id": 1,
      "amount": 100.00,
      "paymentType": "CASH",
      "transactionDate": "2025-12-12T12:12:12.121212",
      "sellerId": 1
    },
    {
      "id": 2,
      "amount": 200.12,
      "paymentType": "CARD",
      "transactionDate": "2025-12-12T12:12:12.121212",
      "sellerId": 1
    },
    {
      "id": 3,
      "amount": 300.24,
      "paymentType": "TRANSFER",
      "transactionDate": "2025-12-12T12:12:12.121212",
      "sellerId": 2
    }
  ],
  "page": {
    "size": 50,
    "number": 0,
    "totalElements": 3,
    "totalPages": 1
  }
}
```
**Endpoint**
``GET /transactions/1``<br>
**Response**<br>
```json
{
  "id": 2,
  "amount": 200.12,
  "paymentType": "CARD",
  "transactionDate": "2025-12-12T12:12:12.121212",
  "sellerId": 1
}
```


**Endpoint**
``POST /transactions``<br>
**Body**<br>
```json
{
  "amount": 200.12,
  "paymentType": "CARD",
  "sellerId": 1
}
```
**Response**
``201 OK``
```json
{
  "id": 2,
  "amount": 200.12,
  "paymentType": "CARD",
  "sellerId": 1,
  "transactionDate": "2025-12-12T12:12:12.121212",
}
```

**Endpoint**
``GET /analytics/getTopSeller?datePeriodType=DAY``<br>
**Response**<br>
```json
[
  {
    "sellerId": 3,
    "sellerName": "test3",
    "totalAmount": 10000.00
  }
]
```

**Endpoint**
``GET /analytics/getBadSellers/?minDate=2023-01-01&maxDate=2025-01-01&minAmount=5000``<br>
**Response**<br>
```json
{
  "content": [
    {
      "id": 1,
      "name": "name",
      "contactInfo": "contact",
      "registrationDate": "2025-25-12T12:12:12.121212"
    },
    {
      "id": 2,
      "name": "name2",
      "contactInfo": "contact2",
      "registrationDate": "2025-25-12T12:12:12.121212"
    }
  ],
  "page": {
    "size": 50,
    "number": 0,
    "totalElements": 2,
    "totalPages": 1
  }
}
```


**Endpoint**
``GET /analytics/getSellerBestPeriod/1``<br>
**Response**<br>
```json
{
  "startOfPeriod": "2025-02-07",
  "endOfPeriod": "2025-02-07",
  "density": 12.00
}
```



