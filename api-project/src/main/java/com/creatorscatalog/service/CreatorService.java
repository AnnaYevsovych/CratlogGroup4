package com.creatorscatalog.service;

import com.creatorscatalog.model.Creator;
import com.creatorscatalog.model.Work;
import com.creatorscatalog.dto.CreatorDto;
import com.creatorscatalog.dto.WorkDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Сервісний шар — in-memory сховище Creator-об'єктів.
 * В реальному проекті замінюється на JPA-репозиторій.
 */
@Service
public class CreatorService {

    private final Map<Integer, Creator> store = new ConcurrentHashMap<>();

    // ---- Creator CRUD ----

    public CreatorDto.CreatorResponse createCreator(CreatorDto.CreateRequest req) {
        if (store.containsKey(req.id)) {
            throw new IllegalArgumentException("Creator з ID=" + req.id + " вже існує");
        }
        Creator c = new Creator(req.id, req.username, req.email, req.password);
        store.put(c.getId(), c);
        return toResponse(c);
    }

    public CreatorDto.CreatorResponse getCreator(int id) {
        return toResponse(findOrThrow(id));
    }

    public List<CreatorDto.CreatorResponse> getAllCreators() {
        return store.values().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public boolean deleteCreator(int id) {
        if (!store.containsKey(id)) {
            throw new NoSuchElementException("Creator з ID=" + id + " не знайдено");
        }
        store.remove(id);
        return true;
    }

    // ---- Auth ----

    public boolean login(int id, String email, String password) {
        return findOrThrow(id).login(email, password);
    }

    public boolean verify(int id, String code) {
        return findOrThrow(id).verify(code);
    }

    public void blockAccount(int id) {
        findOrThrow(id).blockAccount();
    }

    // ---- Portfolio ----

    public WorkDto.WorkResponse addWork(int creatorId, WorkDto.CreateRequest req) {
        Creator c = findOrThrow(creatorId);
        Work w = new Work(req.workId, req.title, req.description, req.imageUrl);
        if (req.tags != null) {
            req.tags.forEach(w::addTag);
        }
        c.addWorkToPortfolio(w);
        return toWorkResponse(w);
    }

    public boolean removeWork(int creatorId, int workId) {
        return findOrThrow(creatorId).getPortfolio().removeWork(workId);
    }

    public List<WorkDto.WorkResponse> getWorks(int creatorId) {
        return findOrThrow(creatorId).getPortfolio().getWorks()
                .stream().map(this::toWorkResponse).collect(Collectors.toList());
    }

    public List<WorkDto.WorkResponse> searchByTag(int creatorId, String tag) {
        return findOrThrow(creatorId).getPortfolio().searchByTag(tag)
                .stream().map(this::toWorkResponse).collect(Collectors.toList());
    }

    public int incrementViews(int creatorId, int count) {
        Creator c = findOrThrow(creatorId);
        c.getPortfolio().incrementViews(count);
        return c.getPortfolio().getTotalViews();
    }

    // ---- helpers ----

    private Creator findOrThrow(int id) {
        Creator c = store.get(id);
        if (c == null) throw new NoSuchElementException("Creator з ID=" + id + " не знайдено");
        return c;
    }

    private CreatorDto.CreatorResponse toResponse(Creator c) {
        return new CreatorDto.CreatorResponse(
                c.getId(), c.getUsername(), c.getEmail(),
                c.isVerified(), c.getStatus().name(),
                c.getPortfolio().getWorksCount()
        );
    }

    private WorkDto.WorkResponse toWorkResponse(Work w) {
        return new WorkDto.WorkResponse(
                w.getWorkId(), w.getTitle(), w.getDescription(),
                w.getImageUrl(), w.getTags()
        );
    }
}
