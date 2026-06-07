import com.creatorscatalog.model.*;
package com.creatorscatalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульні тести для класу Portfolio.
 * Техніки: EP (Equivalence Partitioning), BVA (Boundary Value Analysis).
 * Патерн: AAA (Arrange — Act — Assert).
 */
@DisplayName("Тести класу Portfolio")
class PortfolioTest {

    private Portfolio portfolio;
    private Work work1;
    private Work work2;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio(1);
        work1 = new Work(1, "Логотип для стартапу", "Мінімалістичний лого", "logo.png");
        work2 = new Work(2, "Ілюстрація для книги", "Акварельна ілюстрація", "book.png");
        work1.addTag("logo");
        work1.addTag("branding");
        work2.addTag("illustration");
    }

    // ============================================================
    // Тести конструктора Portfolio
    // ============================================================

    @Test
    @DisplayName("TC-P01: Некоректний ID (BVA: межа 0) — виняток")
    void testConstructor_zeroId_throwsException() {
        // Arrange — вхідний id = 0 (межа BVA: мінімальний недопустимий)
        // Act & Assert
        // Техніка: BVA (межа 0 — недопустиме значення)
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> new Portfolio(0)
        );
        assertTrue(ex.getMessage().contains("додатнім"));
    }

    @Test
    @DisplayName("TC-P02: Від'ємний ID (BVA: межа -1) — виняток")
    void testConstructor_negativeId_throwsException() {
        // Arrange — id = -1 (BVA: значення менше межі)
        // Act & Assert
        // Техніка: BVA (негативне значення)
        assertThrows(IllegalArgumentException.class, () -> new Portfolio(-1));
    }

    @Test
    @DisplayName("TC-P03: Коректний ID (BVA: мінімальний допустимий = 1)")
    void testConstructor_minValidId_createsPortfolio() {
        // Arrange — id = 1 (BVA: мінімальне допустиме)
        // Act
        // Техніка: BVA (мінімальна допустима межа)
        Portfolio p = new Portfolio(1);
        // Assert
        assertEquals(1, p.getPortfolioId());
        assertEquals(0, p.getWorksCount());
    }

    // ============================================================
    // Тести методу addWork
    // ============================================================

    @Test
    @DisplayName("TC-P04: Додавання null-роботи — виняток (EP: недопустимий клас)")
    void testAddWork_nullWork_throwsException() {
        // Arrange
        // Техніка: EP (клас недопустимих значень: null)
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> portfolio.addWork(null));
    }

    @Test
    @DisplayName("TC-P05: Додавання коректної роботи — позитивний тест (EP)")
    void testAddWork_validWork_addsSuccessfully() {
        // Arrange
        // Техніка: EP (позитивний клас — коректний об'єкт Work)
        // Act
        portfolio.addWork(work1);
        // Assert
        assertEquals(1, portfolio.getWorksCount());
        assertEquals(work1, portfolio.getWorks().get(0));
    }

    @Test
    @DisplayName("TC-P06: Додавання роботи з дублікованим ID — виняток (EP: недопустимий клас)")
    void testAddWork_duplicateWorkId_throwsException() {
        // Arrange
        // Техніка: EP (клас дублікатів — недопустима ситуація)
        portfolio.addWork(work1);
        Work duplicate = new Work(1, "Інша назва", "Інший опис", "other.png");
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> portfolio.addWork(duplicate));
    }

    // ============================================================
    // Тести методу removeWork
    // ============================================================

    @Test
    @DisplayName("TC-P07: Видалення роботи з нульовим ID (BVA: межа 0) — виняток")
    void testRemoveWork_zeroId_throwsException() {
        // Arrange
        // Техніка: BVA (межа 0 — недопустиме значення для ID)
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> portfolio.removeWork(0));
    }

    @Test
    @DisplayName("TC-P08: Видалення існуючої роботи — позитивний тест (EP)")
    void testRemoveWork_existingWork_removesSuccessfully() {
        // Arrange
        // Техніка: EP (позитивний клас — робота існує)
        portfolio.addWork(work1);
        // Act
        boolean result = portfolio.removeWork(1);
        // Assert
        assertTrue(result);
        assertEquals(0, portfolio.getWorksCount());
    }

    @Test
    @DisplayName("TC-P09: Видалення неіснуючої роботи — виняток (EP: негативний клас)")
    void testRemoveWork_nonExistentWork_throwsException() {
        // Arrange
        // Техніка: EP (негативний клас — робота відсутня в портфоліо)
        // Act & Assert
        assertThrows(IllegalStateException.class, () -> portfolio.removeWork(999));
    }

    // ============================================================
    // Тести методу searchByTag
    // ============================================================

    @Test
    @DisplayName("TC-P10: Пошук за null-тегом — виняток (EP: недопустимий клас)")
    void testSearchByTag_nullTag_throwsException() {
        // Arrange
        // Техніка: EP (клас недопустимих значень: null)
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> portfolio.searchByTag(null));
    }

    @Test
    @DisplayName("TC-P11: Пошук за порожнім тегом (BVA: порожній рядок) — виняток")
    void testSearchByTag_emptyTag_throwsException() {
        // Arrange
        // Техніка: BVA (порожній рядок — гранична недопустима умова)
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> portfolio.searchByTag("   "));
    }

    @Test
    @DisplayName("TC-P12: Пошук за існуючим тегом — знаходить роботу (EP: позитивний)")
    void testSearchByTag_existingTag_returnsMatchingWorks() {
        // Arrange
        // Техніка: EP (позитивний клас — тег існує)
        portfolio.addWork(work1);
        portfolio.addWork(work2);
        // Act
        List<Work> result = portfolio.searchByTag("logo");
        // Assert
        assertEquals(1, result.size());
        assertEquals(work1.getWorkId(), result.get(0).getWorkId());
    }

    @Test
    @DisplayName("TC-P13: Пошук з різним регістром — нечутливий до регістру (EP)")
    void testSearchByTag_caseInsensitive_returnsMatchingWorks() {
        // Arrange
        // Техніка: EP (клас різних регістрів — поведінка має бути однакова)
        portfolio.addWork(work1);
        // Act
        List<Work> result = portfolio.searchByTag("LOGO");
        // Assert
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("TC-P14: Пошук за відсутнім тегом — повертає порожній список (EP: негативний)")
    void testSearchByTag_nonExistentTag_returnsEmptyList() {
        // Arrange
        // Техніка: EP (негативний клас — тег відсутній)
        portfolio.addWork(work1);
        // Act
        List<Work> result = portfolio.searchByTag("photography");
        // Assert
        assertTrue(result.isEmpty());
    }

    // ============================================================
    // Тести методу incrementViews
    // ============================================================

    @Test
    @DisplayName("TC-P15: Нульові перегляди (BVA: межа 0) — виняток")
    void testIncrementViews_zeroCount_throwsException() {
        // Arrange
        // Техніка: BVA (межа 0 — недопустиме значення)
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> portfolio.incrementViews(0));
    }

    @Test
    @DisplayName("TC-P16: Від'ємні перегляди (BVA: межа -1) — виняток")
    void testIncrementViews_negativeCount_throwsException() {
        // Arrange
        // Техніка: BVA (-1 — значення нижче межі)
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> portfolio.incrementViews(-1));
    }

    @Test
    @DisplayName("TC-P17: Один перегляд (BVA: мінімальне допустиме = 1)")
    void testIncrementViews_oneView_updatesCounter() {
        // Arrange
        // Техніка: BVA (мінімальна допустима межа = 1)
        // Act
        portfolio.incrementViews(1);
        // Assert
        assertEquals(1, portfolio.getTotalViews());
    }

    @Test
    @DisplayName("TC-P18: Кілька переглядів — накопичується (EP: позитивний клас)")
    void testIncrementViews_multipleIncrements_accumulatesCorrectly() {
        // Arrange
        // Техніка: EP (позитивний клас — додатні значення)
        // Act
        portfolio.incrementViews(10);
        portfolio.incrementViews(5);
        // Assert
        assertEquals(15, portfolio.getTotalViews());
    }
}
