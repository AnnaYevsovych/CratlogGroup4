# Лабораторна робота 3
## Модульне тестування програмного коду (Unit Testing)

**Дисципліна:** Основи програмної інженерії  
**Проєкт:** Creators Catalog Platform  
**Мова програмування:** Java 17  
**Фреймворк тестування:** JUnit 5  
**Інструмент покриття:** JaCoCo  

---

## 1. Мета роботи

Набуття практичних навичок із написання модульних тестів із використанням JUnit 5. Оволодіння формальними техніками проєктування тестів — EP та BVA. Отримання досвіду інтерпретації метрик покриття коду та досягнення line coverage ≥ 80 %.

---

## 2. Реалізований модуль

На основі UML-діаграми класів з ЛР 02 (система Creators Catalog) реалізовано три класи:

### `Work.java` — Робота креатора
Методи з нетривіальною логікою:
- Конструктор з валідацією (назва, опис не можуть бути порожніми)
- `addTag(String tag)` — нормалізація до нижнього регістру, перевірка на null
- `updateInfo(String desc)` — перевірка на порожній рядок

### `Portfolio.java` — Портфоліо
Методи з нетривіальною логікою:
- `addWork(Work)` — перевірка на null, дублікат ID, ліміт 50 робіт
- `removeWork(int workId)` — пошук у колекції, обробка відсутності
- `searchByTag(String tag)` — нормалізація, фільтрація, обробка порожнього запиту
- `incrementViews(int count)` — захист від нульових і від'ємних значень

### `Creator.java` — Креатор
Методи з нетривіальною логікою:
- `verify(String code)` — перевірка статусу, формату коду (6 цифр), стану верифікації
- `login(String email, String password)` — нормалізація email, перевірка статусу, null-захист
- `addWorkToPortfolio(Work)` — перевірка верифікації та статусу
- `blockAccount()` — захист від повторного блокування

---

## 3. Таблиця проєктування тестів

| ID | Метод | Вхідні дані | Очікуваний результат | Техніка | Тип |
|----|-------|-------------|----------------------|---------|-----|
| TC-P01 | `Portfolio(id)` | id=0 | `IllegalArgumentException` | BVA (межа 0) | Негативний |
| TC-P02 | `Portfolio(id)` | id=-1 | `IllegalArgumentException` | BVA (межа -1) | Негативний |
| TC-P03 | `Portfolio(id)` | id=1 | Об'єкт створено, worksCount=0 | BVA (мін. допустима межа) | Позитивний |
| TC-P04 | `addWork(w)` | w=null | `IllegalArgumentException` | EP (недопустимий клас: null) | Негативний |
| TC-P05 | `addWork(w)` | коректний Work | worksCount=1 | EP (позитивний клас) | Позитивний |
| TC-P06 | `addWork(w)` | дублікат workId | `IllegalStateException` | EP (клас дублікатів) | Негативний |
| TC-P07 | `removeWork(id)` | id=0 | `IllegalArgumentException` | BVA (межа 0) | Негативний |
| TC-P08 | `removeWork(id)` | id існуючої роботи | true, worksCount=0 | EP (позитивний клас) | Позитивний |
| TC-P09 | `removeWork(id)` | id=999 (відсутній) | `IllegalStateException` | EP (негативний клас) | Негативний |
| TC-P10 | `searchByTag(t)` | t=null | `IllegalArgumentException` | EP (клас null) | Негативний |
| TC-P11 | `searchByTag(t)` | t="   " | `IllegalArgumentException` | BVA (порожній рядок) | Негативний |
| TC-P12 | `searchByTag(t)` | t="logo" (існує) | список з 1 роботи | EP (позитивний клас) | Позитивний |
| TC-P13 | `searchByTag(t)` | t="LOGO" (верхній регістр) | список з 1 роботи | EP (клас різних регістрів) | Позитивний |
| TC-P14 | `searchByTag(t)` | t="photography" (відсутній) | порожній список | EP (негативний клас) | Негативний |
| TC-P15 | `incrementViews(n)` | n=0 | `IllegalArgumentException` | BVA (межа 0) | Негативний |
| TC-P16 | `incrementViews(n)` | n=-1 | `IllegalArgumentException` | BVA (межа -1) | Негативний |
| TC-P17 | `incrementViews(n)` | n=1 | totalViews=1 | BVA (мін. допустима межа) | Позитивний |
| TC-P18 | `incrementViews(n)` | n=10, потім n=5 | totalViews=15 | EP (позитивний клас) | Позитивний |
| TC-C01 | `Creator(...)` | email="not-an-email" | `IllegalArgumentException` | EP (клас некоректних email) | Негативний |
| TC-C02 | `Creator(...)` | password довж.=7 | `IllegalArgumentException` | BVA (межа < 8) | Негативний |
| TC-C03 | `Creator(...)` | password довж.=8 | Об'єкт створено | BVA (мін. допустима межа) | Позитивний |
| TC-C04 | `verify(code)` | code="123456" | true, isVerified=true | EP (позитивний клас) | Позитивний |
| TC-C05 | `verify(code)` | code="12345" (5 цифр) | `IllegalArgumentException` | BVA (межа < 6) | Негативний |
| TC-C06 | `verify(code)` | code="1234567" (7 цифр) | `IllegalArgumentException` | BVA (межа > 6) | Негативний |
| TC-C07 | `verify(code)` | повторна верифікація | `IllegalStateException` | EP (негативний стан) | Негативний |
| TC-C08 | `verify(code)` | акаунт заблоковано | `IllegalStateException` | EP (негативний стан) | Негативний |
| TC-C09 | `login(e, p)` | коректні дані | true | EP (позитивний клас) | Позитивний |
| TC-C10 | `login(e, p)` | неправильний пароль | false | EP (негативний клас) | Негативний |
| TC-C11 | `login(e, p)` | email у верхньому регістрі | true | EP (клас різних регістрів) | Позитивний |
| TC-C12 | `login(e, p)` | password=null | `IllegalArgumentException` | EP (клас null) | Негативний |
| TC-C13 | `login(e, p)` | акаунт заблоковано | `IllegalStateException` | EP (негативний стан) | Негативний |
| TC-C14 | `addWorkToPortfolio` | не верифікований | `IllegalStateException` | EP (негативний стан) | Негативний |
| TC-C15 | `addWorkToPortfolio` | верифікований | worksCount=1 | EP (позитивний стан) | Позитивний |
| TC-C16 | `blockAccount()` | активний акаунт | status=BLOCKED | EP (позитивний стан) | Позитивний |
| TC-C17 | `blockAccount()` | вже заблокований | `IllegalStateException` | EP (негативний стан) | Негативний |
| TC-W01 | `Work(...)` | title="" | `IllegalArgumentException` | EP (порожній рядок) | Негативний |
| TC-W02 | `Work(...)` | description="" | `IllegalArgumentException` | EP (порожній рядок) | Негативний |
| TC-W03 | `Work(...)` | коректні дані | Об'єкт створено | EP (позитивний клас) | Позитивний |
| TC-W04 | `addTag(t)` | t="DESIGN" | tags містить "design" | EP (нормалізація) | Позитивний |
| TC-W05 | `updateInfo(d)` | d="Новий опис" | description оновлено | EP (позитивний клас) | Позитивний |
| TC-W06 | `updateInfo(d)` | d="" | `IllegalArgumentException` | BVA (порожній рядок) | Негативний |

**Загальна кількість тест-кейсів: 41**

---

## 4. Команди запуску

```bash
# Запуск тестів + генерація звіту покриття
mvn test jacoco:report

# Перевірка порогу покриття (≥ 80%)
mvn jacoco:check

# HTML-звіт відкрити у браузері:
# target/site/jacoco/index.html
```

---

## 5. Структура проєкту

```
creators-catalog/
├── pom.xml
├── .github/
│   └── workflows/
│       └── tests.yml          ← GitHub Actions CI
└── src/
    ├── main/java/com/creatorscatalog/
    │   ├── Work.java
    │   ├── Portfolio.java
    │   └── Creator.java
    └── test/java/com/creatorscatalog/
        ├── WorkTest.java       (6 тест-кейсів)
        ├── PortfolioTest.java  (18 тест-кейсів)
        └── CreatorTest.java    (17 тест-кейсів)
```

---

## 6. GitHub Actions (бонус)

Файл `.github/workflows/tests.yml` налаштовує автоматичний запуск при кожному `push` та `pull_request`:
- Checkout репозиторію
- Встановлення JDK 17
- `mvn test jacoco:report` — запуск тестів + генерація звіту
- `mvn jacoco:check` — перевірка порогу покриття ≥ 80%
- Завантаження HTML-звіту як артефакту GitHub Actions

---

## 7. Висновки

В ході лабораторної роботи реалізовано модуль системи **Creators Catalog** мовою Java (3 класи, 11 методів з нетривіальною логікою). Написано **41 тест-кейс** у відповідності до патерну AAA із застосуванням технік EP та BVA. Реалізовано CI-пайплайн через GitHub Actions з автоматичною перевіркою порогу покриття коду ≥ 80 %. Тестування виявило важливість перевірки граничних значень: більшість потенційних помилок виникає саме на межах допустимих діапазонів (id=0, довжина пароля=7/8, код=5/6/7 символів).
