# Creators Catalog REST API — ЛР 05

REST API обгортка над модулем ЛР 02–04 (Creator / Portfolio / Work).

## Технології
- Java 17, Spring Boot 3.2
- Maven
- Docker (бонус)
- GitHub Actions CI/CD (бонус)

---

## Швидкий запуск локально

```bash
mvn spring-boot:run
```

Сервер: http://localhost:8080

---

## Endpoints

| Метод | URL | Опис |
|-------|-----|------|
| GET | `/health` | Health check |
| GET | `/api/creators` | Список усіх креаторів |
| POST | `/api/creators` | Створити креатора |
| GET | `/api/creators/{id}` | Отримати за ID |
| DELETE | `/api/creators/{id}` | Видалити |
| POST | `/api/creators/{id}/login` | Вхід (email + password) |
| POST | `/api/creators/{id}/verify` | Верифікація (6-значний код) |
| POST | `/api/creators/{id}/block` | Заблокувати акаунт |
| GET | `/api/creators/{id}/works` | Список робіт портфоліо |
| POST | `/api/creators/{id}/works` | Додати роботу |
| DELETE | `/api/creators/{id}/works/{workId}` | Видалити роботу |
| GET | `/api/creators/{id}/works/search?tag=X` | Пошук за тегом |
| POST | `/api/creators/{id}/portfolio/views?count=N` | Інкремент переглядів |

---

## Приклади запитів (curl)

### Health check
```bash
curl http://localhost:8080/health
```

### Створити креатора
```bash
curl -X POST http://localhost:8080/api/creators \
  -H "Content-Type: application/json" \
  -d '{"id":1,"username":"anna","email":"anna@example.com","password":"securepass"}'
```

### Верифікувати
```bash
curl -X POST http://localhost:8080/api/creators/1/verify \
  -H "Content-Type: application/json" \
  -d '{"code":"123456"}'
```

### Додати роботу до портфоліо
```bash
curl -X POST http://localhost:8080/api/creators/1/works \
  -H "Content-Type: application/json" \
  -d '{"workId":10,"title":"Poster Design","description":"A concert poster","imageUrl":"https://example.com/img.jpg","tags":["design","poster"]}'
```

### Пошук за тегом
```bash
curl "http://localhost:8080/api/creators/1/works/search?tag=design"
```

---

## Docker (бонус)

```bash
docker build -t creators-catalog-api .
docker run -p 8080:8080 creators-catalog-api
```

---

## Деплой на Render

1. Підключи GitHub-репозиторій на https://render.com
2. New → Web Service → обери репозиторій
3. Build Command: `mvn -B package -DskipTests`
4. Start Command: `java -jar target/creators-catalog-api-1.0.0.jar`
5. Environment Variable: `PORT` = `8080` (Render виставляє автоматично)
6. Deploy!

Для CI/CD через GitHub Actions — додай Secrets:
- `RENDER_DEPLOY_HOOK_URL` — Deploy Hook URL з Render Dashboard
- `DOCKER_USERNAME` / `DOCKER_PASSWORD` — якщо використовуєш Docker Hub
