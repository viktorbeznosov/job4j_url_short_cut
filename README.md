# URL Shortcut Service

Сервис для сокращения ссылок с поддержкой регистрации сайтов, JWT авторизации и статистики переходов.

## Технологии

- Java 17
- Spring Boot 3.1.5
- PostgreSQL
- JWT (JSON Web Token)
- BCrypt для хеширования паролей

## Установка и запуск

### 1. Создание базы данных

```sql
CREATE DATABASE url_shortcut;
```

### 2. Выполнение SQL скрипта

Выполните скрипт `src/main/resources/schema.sql` для создания таблиц:

```bash
psql -U postgres -d url_shortcut -f src/main/resources/schema.sql
```

### 3. Настройка подключения

Отредактируйте `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/url_shortcut
    username: postgres
    password: YOUR_PASSWORD
```

### 4. Сборка и запуск

```bash
mvn clean install
mvn spring-boot:run
```

Приложение запустится на порту 8080.

## API Endpoints

### 1. Регистрация сайта

**POST** `/api/registration`

Регистрирует новый сайт в системе. Если сайт уже зарегистрирован, возвращает существующие учетные данные.

**Request:**
```json
{
    "site": "job4j.ru"
}
```

**Response:**
```json
{
    "registration": true,
    "login": "550e8400-e29b-41d4-a716-446655440000",
    "password": "660e8400-e29b-41d4-a716-446655440000"
}
```

- `registration: true` - новый сайт зарегистрирован
- `registration: false` - сайт уже был зарегистрирован

### 2. Авторизация

**POST** `/api/auth/login`

Получение JWT токена для авторизации.

**Request:**
```json
{
    "login": "550e8400-e29b-41d4-a716-446655440000",
    "password": "660e8400-e29b-41d4-a716-446655440000"
}
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Использование токена:**

В последующих запросах добавьте заголовок:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### 3. Конвертация URL

**POST** `/api/convert` (требуется авторизация)

Преобразует длинный URL в короткий код.

**Request:**
```json
{
    "url": "https://job4j.ru/profile/exercise/106/task-view/532"
}
```

**Response:**
```json
{
    "code": "ZRUfdD2"
}
```

### 4. Переадресация

**GET** `/api/redirect/{code}` (без авторизации)

Выполняет редирект на оригинальный URL. Возвращает HTTP 302 с заголовком `Location`.

**Пример:**
```
GET /api/redirect/ZRUfdD2
```

**Response:**
```
HTTP 302
Location: https://job4j.ru/profile/exercise/106/task-view/532
```

### 5. Статистика

**GET** `/api/statistic` (требуется авторизация)

Возвращает статистику переходов по всем URL сайта.

**Response:**
```json
[
    {
        "url": "https://job4j.ru/profile/exercise/106/task-view/532",
        "total": 103
    },
    {
        "url": "https://job4j.ru/profile/exercise/107/task-view/600",
        "total": 45
    }
]
```

## Структура базы данных

### Таблица `sites`

Хранит зарегистрированные сайты и учетные данные.

| Поле | Тип | Описание |
|------|-----|----------|
| id | bigserial | Primary key |
| site | varchar(255) | Домен сайта (уникальный) |
| login | varchar(120) | Уникальный логин |
| password_hash | varchar(255) | Хеш пароля (BCrypt) |
| created_at | timestamp | Дата регистрации |

### Таблица `urls`

Хранит сокращенные ссылки и статистику.

| Поле | Тип | Описание |
|------|-----|----------|
| id | bigserial | Primary key |
| site_id | bigint | Foreign key -> sites(id) |
| original_url | text | Оригинальный URL |
| code | varchar(32) | Короткий код (уникальный) |
| total_visits | bigint | Счетчик переходов |
| created_at | timestamp | Дата создания |

## Важные особенности

### Счетчик переходов

Счетчик `total_visits` увеличивается **в базе данных**, а не в Java коде:

```java
@Modifying
@Query("UPDATE Url u SET u.totalVisits = u.totalVisits + 1 WHERE u.code = :code")
int incrementVisits(String code);
```

**Почему в БД?**
- Избежание race conditions при одновременных запросах
- Атомарность операции на уровне СУБД
- Не нужна блокировка в приложении

### Безопасность

- Пароли хранятся в виде BCrypt хешей
- Авторизация через JWT токены
- Внешние ключи обеспечивают целостность данных
- `ON DELETE CASCADE` - при удалении сайта удаляются все его ссылки

## Пример использования

```bash
# 1. Регистрация сайта
curl -X POST http://localhost:8080/api/registration \
  -H "Content-Type: application/json" \
  -d '{"site": "job4j.ru"}'

# 2. Получение токена
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login": "ваш_логин", "password": "ваш_пароль"}'

# 3. Конвертация URL
curl -X POST http://localhost:8080/api/convert \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ваш_токен" \
  -d '{"url": "https://job4j.ru/profile/exercise/106/task-view/532"}'

# 4. Переадресация (в браузере или с флагом -L)
curl -L http://localhost:8080/api/redirect/ZRUfdD2

# 5. Статистика
curl http://localhost:8080/api/statistic \
  -H "Authorization: Bearer ваш_токен"
```
