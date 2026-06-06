package com.creatorscatalog;

/**
 * Клас Creator — спадкоємець User, представляє креатора платформи.
 * Відповідає класу Creator з UML-діаграми класів ЛР 02.
 * Містить логіку верифікації, управління портфоліо,
 * блокування облікового запису.
 */
public class Creator {

    public enum Status { ACTIVE, BLOCKED }

    private final int id;
    private final String username;
    private String email;
    private String passwordHash;
    private boolean isVerified;
    private Status status;
    private final Portfolio portfolio;

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String EMAIL_REGEX = "^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
    private static final java.util.regex.Pattern EMAIL_PATTERN = java.util.regex.Pattern.compile(EMAIL_REGEX);

    public Creator(int id, String username, String email, String password) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID має бути додатнім числом");
        }
        validateEmail(email);
        validatePassword(password);

        this.id = id;
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username не може бути порожнім");
        }
        this.username = username;
        this.email = email.trim().toLowerCase();
        this.passwordHash = hashPassword(password);
        this.isVerified = false;
        this.status = Status.ACTIVE;
        this.portfolio = new Portfolio(id);
    }

    // -------------------------------------------------------
    // Метод 1: register — логіка верифікації акаунту
    // Нетривіальна логіка: перевірка статусу, умовні переходи
    // -------------------------------------------------------

    /**
     * Активує верифікацію облікового запису креатора.
     *
     * @param verificationCode код верифікації (6 цифр)
     * @return true — якщо верифікацію пройдено успішно
     * @throws IllegalStateException    якщо акаунт заблоковано або вже верифіковано
     * @throws IllegalArgumentException якщо код некоректний
     */
    public boolean verify(String verificationCode) {
        if (status == Status.BLOCKED) {
            throw new IllegalStateException("Неможливо верифікувати заблокований акаунт");
        }
        if (isVerified) {
            throw new IllegalStateException("Акаунт вже верифіковано");
        }
        if (verificationCode == null || !verificationCode.matches("\\d{6}")) {
            throw new IllegalArgumentException("Код верифікації має містити рівно 6 цифр");
        }
        // У реальній системі тут відбувається порівняння з кодом з БД/email
        // Для спрощення приймаємо будь-який 6-значний код як коректний
        this.isVerified = true;
        return true;
    }

    // -------------------------------------------------------
    // Метод 2: login — перевірка пари email/пароль
    // Нетривіальна логіка: нормалізація email, хешування,
    // перевірка статусу, обробка null
    // -------------------------------------------------------

    /**
     * Виконує вхід у систему — перевіряє email та пароль.
     *
     * @param inputEmail    введений email
     * @param inputPassword введений пароль
     * @return true — якщо дані коректні та акаунт активний
     * @throws IllegalStateException    якщо акаунт заблоковано
     * @throws IllegalArgumentException якщо email або пароль null
     */
    public boolean login(String inputEmail, String inputPassword) {
        if (inputEmail == null || inputPassword == null) {
            throw new IllegalArgumentException("Email та пароль не можуть бути null");
        }
        if (status == Status.BLOCKED) {
            throw new IllegalStateException("Акаунт заблоковано. Зверніться до адміністратора");
        }
        String normalizedEmail = inputEmail.trim().toLowerCase();
        String inputHash = hashPassword(inputPassword);
        return this.email.equals(normalizedEmail) && this.passwordHash.equals(inputHash);
    }

    // -------------------------------------------------------
    // Метод 3: addWorkToPortfolio — додавання роботи
    // Нетривіальна логіка: перевірка верифікації та статусу
    // -------------------------------------------------------

    /**
     * Додає роботу до портфоліо креатора.
     * Вимагає верифікованого та активного акаунту (FR-05).
     *
     * @param work робота для додавання
     * @throws IllegalStateException якщо креатор не верифікований або заблокований
     */
    public void addWorkToPortfolio(Work work) {
        if (status == Status.BLOCKED) {
            throw new IllegalStateException("Заблокований акаунт не може додавати роботи");
        }
        if (!isVerified) {
            throw new IllegalStateException("Лише верифіковані креатори можуть додавати роботи");
        }
        portfolio.addWork(work);
    }

    // -------------------------------------------------------
    // Метод 4: blockAccount — блокування адміністратором
    // -------------------------------------------------------

    /**
     * Блокує обліковий запис (FR-06, виконує Admin).
     *
     * @throws IllegalStateException якщо акаунт вже заблоковано
     */
    public void blockAccount() {
        if (status == Status.BLOCKED) {
            throw new IllegalStateException("Акаунт вже заблоковано");
        }
        this.status = Status.BLOCKED;
    }

    // --- Приватні допоміжні методи ---

    private void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Некоректний формат email: " + email);
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                "Пароль має містити щонайменше " + MIN_PASSWORD_LENGTH + " символів"
            );
        }
    }

    private String hashPassword(String password) {
        // Спрощений хеш для демонстрації (у продакшені — BCrypt або SHA-256)
        return "HASH_" + password.length() + "_" + password.hashCode();
    }

    // --- Getters ---

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public boolean isVerified() { return isVerified; }
    public Status getStatus() { return status; }
    public Portfolio getPortfolio() { return portfolio; }
}
