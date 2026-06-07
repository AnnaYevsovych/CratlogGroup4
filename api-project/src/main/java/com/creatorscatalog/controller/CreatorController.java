package com.creatorscatalog.controller;

import com.creatorscatalog.dto.CreatorDto;
import com.creatorscatalog.dto.WorkDto;
import com.creatorscatalog.service.CreatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * REST-контролер для API Creators Catalog.
 *
 * Основні ендпоінти:
 *   GET    /api/creators               — список усіх креаторів
 *   POST   /api/creators               — створити креатора
 *   GET    /api/creators/{id}          — отримати за ID
 *   DELETE /api/creators/{id}          — видалити
 *   POST   /api/creators/{id}/login    — вхід
 *   POST   /api/creators/{id}/verify   — верифікація
 *   POST   /api/creators/{id}/block    — заблокувати
 *   GET    /api/creators/{id}/works    — список робіт
 *   POST   /api/creators/{id}/works    — додати роботу
 *   DELETE /api/creators/{id}/works/{workId} — видалити роботу
 *   GET    /api/creators/{id}/works/search?tag=... — пошук за тегом
 *   POST   /api/creators/{id}/portfolio/views — інкремент переглядів
 */
@RestController
@RequestMapping("/api/creators")
public class CreatorController {

    private final CreatorService service;

    public CreatorController(CreatorService service) {
        this.service = service;
    }

    // ---------- CRUD ----------

    @GetMapping
    public List<CreatorDto.CreatorResponse> getAll() {
        return service.getAllCreators();
    }

    @PostMapping
    public ResponseEntity<CreatorDto.CreatorResponse> create(
            @RequestBody CreatorDto.CreateRequest req) {
        CreatorDto.CreatorResponse created = service.createCreator(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public CreatorDto.CreatorResponse getById(@PathVariable int id) {
        return service.getCreator(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        service.deleteCreator(id);
        return ResponseEntity.ok(Map.of("deleted", true, "id", id));
    }

    // ---------- Auth ----------

    @PostMapping("/{id}/login")
    public ResponseEntity<Map<String, Object>> login(
            @PathVariable int id,
            @RequestBody CreatorDto.LoginRequest req) {
        boolean ok = service.login(id, req.email, req.password);
        return ResponseEntity.ok(Map.of("success", ok));
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<Map<String, Object>> verify(
            @PathVariable int id,
            @RequestBody CreatorDto.VerifyRequest req) {
        boolean ok = service.verify(id, req.code);
        return ResponseEntity.ok(Map.of("verified", ok));
    }

    @PostMapping("/{id}/block")
    public ResponseEntity<Map<String, Object>> block(@PathVariable int id) {
        service.blockAccount(id);
        return ResponseEntity.ok(Map.of("blocked", true, "creatorId", id));
    }

    // ---------- Portfolio / Works ----------

    @GetMapping("/{id}/works")
    public List<WorkDto.WorkResponse> getWorks(@PathVariable int id) {
        return service.getWorks(id);
    }

    @PostMapping("/{id}/works")
    public ResponseEntity<WorkDto.WorkResponse> addWork(
            @PathVariable int id,
            @RequestBody WorkDto.CreateRequest req) {
        WorkDto.WorkResponse created = service.addWork(id, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}/works/{workId}")
    public ResponseEntity<Map<String, Object>> removeWork(
            @PathVariable int id,
            @PathVariable int workId) {
        service.removeWork(id, workId);
        return ResponseEntity.ok(Map.of("deleted", true, "workId", workId));
    }

    @GetMapping("/{id}/works/search")
    public List<WorkDto.WorkResponse> searchByTag(
            @PathVariable int id,
            @RequestParam String tag) {
        return service.searchByTag(id, tag);
    }

    @PostMapping("/{id}/portfolio/views")
    public ResponseEntity<Map<String, Object>> incrementViews(
            @PathVariable int id,
            @RequestParam(defaultValue = "1") int count) {
        int total = service.incrementViews(id, count);
        return ResponseEntity.ok(Map.of("totalViews", total));
    }

    // ---------- Global error handling ----------

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
}
