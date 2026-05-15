package com.creatorscatalog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Клас Portfolio — агрегує роботи (Work) креатора.
 * Відповідає класу Portfolio з UML-діаграми класів ЛР 02.
 * Містить нетривіальну логіку: додавання/видалення робіт,
 * пошук за тегом, підрахунок переглядів.
 */
public class Portfolio {

    private final int portfolioId;
    private int totalViews;
    private final List<Work> works;

    private static final int MAX_WORKS = 50;

    public Portfolio(int portfolioId) {
        if (portfolioId <= 0) {
            throw new IllegalArgumentException("ID портфоліо має бути додатнім числом");
        }
        this.portfolioId = portfolioId;
        this.totalViews = 0;
        this.works = new ArrayList<>();
    }

    // -------------------------------------------------------
    // Метод 1: addWork — додавання роботи до портфоліо
    // Нетривіальна логіка: перевірка дублікатів, ліміту,
    // валідація вхідних даних
    // -------------------------------------------------------

    /**
     * Додає роботу до портфоліо.
     *
     * @param work робота для додавання
     * @throws IllegalArgumentException якщо work == null
     * @throws IllegalStateException    якщо портфоліо переповнене або робота вже є
     */
    public void addWork(Work work) {
        if (work == null) {
            throw new IllegalArgumentException("Робота не може бути null");
        }
        if (works.size() >= MAX_WORKS) {
            throw new IllegalStateException(
                "Портфоліо переповнене: максимум " + MAX_WORKS + " робіт"
            );
        }
        boolean alreadyExists = works.stream()
            .anyMatch(w -> w.getWorkId() == work.getWorkId());
        if (alreadyExists) {
            throw new IllegalStateException(
                "Робота з ID=" + work.getWorkId() + " вже є в портфоліо"
            );
        }
        works.add(work);
    }

    // -------------------------------------------------------
    // Метод 2: removeWork — видалення роботи за ID
    // Нетривіальна логіка: пошук у колекції, обробка випадку
    // відсутності елементу
    // -------------------------------------------------------

    /**
     * Видаляє роботу з портфоліо за її ID.
     *
     * @param workId ідентифікатор роботи
     * @return true — якщо роботу знайдено та видалено
     * @throws IllegalArgumentException якщо workId <= 0
     * @throws IllegalStateException    якщо роботу з таким ID не знайдено
     */
    public boolean removeWork(int workId) {
        if (workId <= 0) {
            throw new IllegalArgumentException("ID роботи має бути додатнім числом");
        }
        Work found = works.stream()
            .filter(w -> w.getWorkId() == workId)
            .findFirst()
            .orElse(null);
        if (found == null) {
            throw new IllegalStateException("Роботу з ID=" + workId + " не знайдено");
        }
        return works.remove(found);
    }

    // -------------------------------------------------------
    // Метод 3: searchByTag — пошук робіт за тегом
    // Нетривіальна логіка: фільтрація, нормалізація рядка,
    // обробка порожнього запиту
    // -------------------------------------------------------

    /**
     * Шукає роботи у портфоліо за тегом (без урахування регістру).
     *
     * @param tag рядок-тег для пошуку
     * @return список робіт, що містять заданий тег
     * @throws IllegalArgumentException якщо tag null або порожній
     */
    public List<Work> searchByTag(String tag) {
        if (tag == null || tag.isBlank()) {
            throw new IllegalArgumentException("Тег для пошуку не може бути порожнім");
        }
        String normalizedTag = tag.trim().toLowerCase();
        return works.stream()
            .filter(w -> w.getTags().contains(normalizedTag))
            .collect(Collectors.toList());
    }

    // -------------------------------------------------------
    // Метод 4: incrementViews — лічильник переглядів
    // Нетривіальна логіка: захист від від'ємного значення
    // -------------------------------------------------------

    /**
     * Збільшує лічильник переглядів портфоліо.
     *
     * @param count кількість переглядів для додавання (має бути > 0)
     * @throws IllegalArgumentException якщо count <= 0
     */
    public void incrementViews(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException(
                "Кількість переглядів має бути більше нуля"
            );
        }
        this.totalViews += count;
    }

    // --- Getters ---

    public int getPortfolioId() { return portfolioId; }
    public int getTotalViews() { return totalViews; }
    public List<Work> getWorks() { return new ArrayList<>(works); }
    public int getWorksCount() { return works.size(); }
}
