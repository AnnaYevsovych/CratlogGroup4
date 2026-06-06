package com.creatorscatalog;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас Work — представляє роботу (портфоліо-елемент) креатора.
 * Відповідає класу Work з UML-діаграми класів ЛР 02.
 */
public class Work {
    private int workId;
    private String title;
    private String description;
    private String imageUrl;
    private List<String> tags;

    public Work(int workId, String title, String description, String imageUrl) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Назва роботи не може бути порожньою");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Опис роботи не може бути порожнім");
        }
        this.workId = workId;
        this.title = title.trim();
        this.description = description.trim();
        this.imageUrl = imageUrl;
        this.tags = new ArrayList<>();
    }

    public int getWorkId() { return workId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public List<String> getTags() { return new ArrayList<>(tags); }

    public void addTag(String tag) {
        if (tag == null || tag.isBlank()) {
            throw new IllegalArgumentException("Тег не може бути порожнім");
        }
        tags.add(tag.trim().toLowerCase());
    }

    public String getDetails() {
        return String.format("Work[id=%d, title='%s', tags=%s]", workId, title, tags);
    }

    public void updateInfo(String newDescription) {
        if (newDescription == null || newDescription.isBlank()) {
            throw new IllegalArgumentException("Новий опис не може бути порожнім");
        }
        this.description = newDescription.trim();
    }
}
