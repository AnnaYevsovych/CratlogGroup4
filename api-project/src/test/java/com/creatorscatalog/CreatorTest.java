import com.creatorscatalog.model.*;
package com.creatorscatalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульні тести для класу Creator.
 * Техніки: EP (Equivalence Partitioning), BVA (Boundary Value Analysis).
 * Патерн: AAA (Arrange — Act — Assert).
 */
@DisplayName("Тести класу Creator")
class CreatorTest {

    private Creator creator;

    @BeforeEach
    void setUp() {
        creator = new Creator(1, "anna_art", "anna@example.com", "securePass123");
    }

    // ============================================================
    // Тести конструктора Creator
    // ============================================================

    @Test
    @DisplayName("TC-C01: Некоректний email — виняток (EP: недопустимий клас)")
    void testConstructor_invalidEmail_throwsException() {
        // Arrange
        // Техніка: EP (клас некоректних email — без @)
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> new Creator(1, "user", "not-an-email", "password123"));
    }

    @Test
    @DisplayName("TC-C02: Короткий пароль (BVA: довжина 7, межа < 8) — виняток")
    void testConstructor_shortPassword_throwsException() {
        // Arrange
        // Техніка: BVA (довжина 7 — на 1 менше мінімуму)
        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new Creator(1, "user", "user@example.com", "pass123"));
        assertTrue(ex.getMessage().contains("8"));
    }

    @Test
    @DisplayName("TC-C03: Пароль рівно 8 символів (BVA: мінімальна допустима межа)")
    void testConstructor_exactly8CharPassword_createsCreator() {
        // Arrange
        // Техніка: BVA (межа 8 — мінімальний допустимий пароль)
        // Act
        Creator c = new Creator(2, "user2", "user2@example.com", "pass1234");
        // Assert
        assertEquals("user2", c.getUsername());
        assertFalse(c.isVerified());
        assertEquals(Creator.Status.ACTIVE, c.getStatus());
    }

    // ============================================================
    // Тести методу verify
    // ============================================================

    @Test
    @DisplayName("TC-C04: Коректний 6-значний код — верифікація успішна (EP: позитивний)")
    void testVerify_validCode_returnsTrue() {
        // Arrange
        // Техніка: EP (позитивний клас — 6 цифр)
        // Act
        boolean result = creator.verify("123456");
        // Assert
        assertTrue(result);
        assertTrue(creator.isVerified());
    }

    @Test
    @DisplayName("TC-C05: Код з 5 цифр (BVA: межа < 6) — виняток")
    void testVerify_fiveDigitCode_throwsException() {
        // Arrange
        // Техніка: BVA (5 символів — менше допустимої межі)
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> creator.verify("12345"));
    }

    @Test
    @DisplayName("TC-C06: Код з 7 цифр (BVA: межа > 6) — виняток")
    void testVerify_sevenDigitCode_throwsException() {
        // Arrange
        // Техніка: BVA (7 символів — більше допустимої межі)
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> creator.verify("1234567"));
    }

    @Test
    @DisplayName("TC-C07: Повторна верифікація — виняток (EP: негативний стан)")
    void testVerify_alreadyVerified_throwsException() {
        // Arrange
        // Техніка: EP (негативний клас — акаунт вже верифіковано)
        creator.verify("123456");
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> creator.verify("654321"));
    }

    @Test
    @DisplayName("TC-C08: Верифікація заблокованого акаунту — виняток (EP: негативний стан)")
    void testVerify_blockedAccount_throwsException() {
        // Arrange
        // Техніка: EP (негативний клас — акаунт заблоковано)
        creator.blockAccount();
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> creator.verify("123456"));
    }

    // ============================================================
    // Тести методу login
    // ============================================================

    @Test
    @DisplayName("TC-C09: Коректні дані входу — успіх (EP: позитивний клас)")
    void testLogin_correctCredentials_returnsTrue() {
        // Arrange
        // Техніка: EP (позитивний клас — правильні email та пароль)
        // Act
        boolean result = creator.login("anna@example.com", "securePass123");
        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("TC-C10: Неправильний пароль — повертає false (EP: негативний клас)")
    void testLogin_wrongPassword_returnsFalse() {
        // Arrange
        // Техніка: EP (негативний клас — неправильний пароль)
        // Act
        boolean result = creator.login("anna@example.com", "wrongPassword");
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("TC-C11: Email у верхньому регістрі — успіх (нормалізація, EP)")
    void testLogin_uppercaseEmail_returnsTrue() {
        // Arrange
        // Техніка: EP (клас різних регістрів — нормалізація має відбутись)
        // Act
        boolean result = creator.login("ANNA@EXAMPLE.COM", "securePass123");
        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("TC-C12: null замість пароля — виняток (EP: недопустимий клас)")
    void testLogin_nullPassword_throwsException() {
        // Arrange
        // Техніка: EP (клас недопустимих значень: null)
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> creator.login("anna@example.com", null));
    }

    @Test
    @DisplayName("TC-C13: Вхід до заблокованого акаунту — виняток (EP: негативний стан)")
    void testLogin_blockedAccount_throwsException() {
        // Arrange
        // Техніка: EP (негативний клас — акаунт заблоковано)
        creator.blockAccount();
        // Act & Assert
        assertThrows(IllegalStateException.class,
            () -> creator.login("anna@example.com", "securePass123"));
    }

    // ============================================================
    // Тести методу addWorkToPortfolio
    // ============================================================

    @Test
    @DisplayName("TC-C14: Невірефікований креатор додає роботу — виняток (EP: негативний стан)")
    void testAddWorkToPortfolio_notVerified_throwsException() {
        // Arrange
        // Техніка: EP (негативний клас — не верифікований)
        Work work = new Work(1, "Лого", "Опис", "img.png");
        // Act & Assert
        assertThrows(IllegalStateException.class,
            () -> creator.addWorkToPortfolio(work));
    }

    @Test
    @DisplayName("TC-C15: Верифікований креатор додає роботу — успіх (EP: позитивний)")
    void testAddWorkToPortfolio_verifiedCreator_addsWork() {
        // Arrange
        // Техніка: EP (позитивний клас — верифікований + активний)
        creator.verify("123456");
        Work work = new Work(1, "Лого", "Мінімалістичний", "img.png");
        // Act
        creator.addWorkToPortfolio(work);
        // Assert
        assertEquals(1, creator.getPortfolio().getWorksCount());
    }

    // ============================================================
    // Тести методу blockAccount
    // ============================================================

    @Test
    @DisplayName("TC-C16: Блокування активного акаунту — успіх (EP: позитивний стан)")
    void testBlockAccount_activeAccount_becomesBlocked() {
        // Arrange
        // Техніка: EP (позитивний клас — активний акаунт)
        // Act
        creator.blockAccount();
        // Assert
        assertEquals(Creator.Status.BLOCKED, creator.getStatus());
    }

    @Test
    @DisplayName("TC-C17: Повторне блокування — виняток (EP: негативний стан)")
    void testBlockAccount_alreadyBlocked_throwsException() {
        // Arrange
        // Техніка: EP (негативний клас — вже заблокований)
        creator.blockAccount();
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> creator.blockAccount());
    }
}
