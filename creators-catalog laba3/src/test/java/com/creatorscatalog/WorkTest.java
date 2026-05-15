package com.creatorscatalog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульні тести для класу Work.
 * Техніки: EP (Equivalence Partitioning), BVA (Boundary Value Analysis).
 * Патерн: AAA (Arrange — Act — Assert).
 */
@DisplayName("Тести класу Work")
class WorkTest {

    @Test
    @DisplayName("TC-W01: Порожня назва роботи — виняток (EP: недопустимий клас)")
    void testConstructor_emptyTitle_throwsException() {
        // Arrange
        // Техніка: EP (клас порожніх рядків — недопустимий)
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> new Work(1, "   ", "Опис", "img.png"));
    }

    @Test
    @DisplayName("TC-W02: Порожній опис — виняток (EP: недопустимий клас)")
    void testConstructor_emptyDescription_throwsException() {
        // Arrange
        // Техніка: EP (клас порожніх рядків — недопустимий)
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> new Work(1, "Назва", "", "img.png"));
    }

    @Test
    @DisplayName("TC-W03: Коректні дані — робота створена (EP: позитивний клас)")
    void testConstructor_validData_createsWork() {
        // Arrange
        // Техніка: EP (позитивний клас — всі дані коректні)
        // Act
        Work work = new Work(1, "Мій лого", "Опис логотипу", "logo.png");
        // Assert
        assertEquals(1, work.getWorkId());
        assertEquals("Мій лого", work.getTitle());
        assertTrue(work.getTags().isEmpty());
    }

    @Test
    @DisplayName("TC-W04: Додавання коректного тегу — успіх (EP: позитивний)")
    void testAddTag_validTag_addsTag() {
        // Arrange
        // Техніка: EP (позитивний клас — непорожній рядок)
        Work work = new Work(1, "Назва", "Опис", "img.png");
        // Act
        work.addTag("DESIGN");
        // Assert
        assertTrue(work.getTags().contains("design")); // нормалізація до нижнього регістру
    }

    @Test
    @DisplayName("TC-W05: Оновлення опису — успіх (EP: позитивний)")
    void testUpdateInfo_validDescription_updatesDescription() {
        // Arrange
        // Техніка: EP (позитивний клас — непорожній рядок)
        Work work = new Work(1, "Назва", "Старий опис", "img.png");
        // Act
        work.updateInfo("Новий детальний опис роботи");
        // Assert
        assertEquals("Новий детальний опис роботи", work.getDescription());
    }

    @Test
    @DisplayName("TC-W06: Оновлення з порожнім описом — виняток (BVA: порожній рядок)")
    void testUpdateInfo_emptyDescription_throwsException() {
        // Arrange
        // Техніка: BVA (порожній рядок — гранична недопустима умова)
        Work work = new Work(1, "Назва", "Опис", "img.png");
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> work.updateInfo(""));
    }
}
