package com.java.campus.controller;

import com.java.campus.model.*;
import com.java.campus.service.CampusService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CampusController {

    private final CampusService campusService;

    public CampusController(CampusService campusService) {
        this.campusService = campusService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        return new ResponseEntity<>(campusService.registerUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/users/login")
    public ResponseEntity<User> login(@RequestParam String email) {
        return ResponseEntity.ok(campusService.loginUser(email));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(campusService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(campusService.getUserById(id));
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        return new ResponseEntity<>(campusService.createCategory(category), HttpStatus.CREATED);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(campusService.getAllCategories());
    }

    @PostMapping("/lost-reports")
    public ResponseEntity<LostReport> createLostReport(@Valid @RequestBody LostReport report) {
        return new ResponseEntity<>(campusService.createLostReport(report), HttpStatus.CREATED);
    }

    @GetMapping("/lost-reports")
    public ResponseEntity<List<LostReport>> getAllLostReports(@RequestParam(required = false) Long categoryId) {
        if (categoryId != null) {
            return ResponseEntity.ok(campusService.getLostReportsByCategory(categoryId));
        }
        return ResponseEntity.ok(campusService.getAllLostReports());
    }

    @GetMapping("/lost-reports/{id}/matches")
    public ResponseEntity<List<FoundItem>> getMatchesForLostReport(@PathVariable Long id) {
        return ResponseEntity.ok(campusService.findMatchesForLostReport(id));
    }

    @PostMapping("/found-items")
    public ResponseEntity<FoundItem> createFoundItem(@Valid @RequestBody FoundItem item) {
        return new ResponseEntity<>(campusService.createFoundItem(item), HttpStatus.CREATED);
    }

    @GetMapping("/found-items")
    public ResponseEntity<List<FoundItem>> getAllFoundItems(@RequestParam(required = false) ItemStatus status) {
        if (status != null) {
            return ResponseEntity.ok(campusService.getFoundItemsByStatus(status));
        }
        return ResponseEntity.ok(campusService.getAllFoundItems());
    }

    @GetMapping("/matches")
    public ResponseEntity<List<FoundItem>> getMatches(@RequestParam Long categoryId,
                                                      @RequestParam(required = false, defaultValue = "") String keyword) {
        return ResponseEntity.ok(campusService.findMatches(categoryId, keyword));
    }

    @PutMapping("/found-items/{id}/status")
    public ResponseEntity<FoundItem> updateStatus(@PathVariable Long id,
                                                   @RequestParam ItemStatus status,
                                                   @RequestParam Long requesterUserId) {
        return ResponseEntity.ok(campusService.updateItemStatus(id, status, requesterUserId));
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(campusService.getAdminDashboardMetrics());
    }
}