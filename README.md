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
    - Обновление транзакции по id
    - Удаление транзакции по id


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
   - Запуск контейнера базы данных <pre>docker-compose -up -d postgres</pre><br>
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




