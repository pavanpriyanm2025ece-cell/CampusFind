package com.java.campus.service;

import com.java.campus.model.*;
import com.java.campus.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CampusService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LostReportRepository lostReportRepository;
    private final FoundItemRepository foundItemRepository;

    public CampusService(UserRepository userRepository,
                         CategoryRepository categoryRepository,
                         LostReportRepository lostReportRepository,
                         FoundItemRepository foundItemRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.lostReportRepository = lostReportRepository;
        this.foundItemRepository = foundItemRepository;
    }

    public User loginUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    public User registerUser(User user) {
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email '" + user.getEmail() + "' already exists.");
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    public Category createCategory(Category category) {
        if (category.getName() != null && categoryRepository.findByNameIgnoreCase(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Category '" + category.getName() + "' already exists.");
        }
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public LostReport createLostReport(LostReport report) {
        if (report.getReporter() != null && report.getReporter().getId() != null) {
            report.setReporter(getUserById(report.getReporter().getId()));
        }
        if (report.getCategory() != null && report.getCategory().getId() != null) {
            report.setCategory(categoryRepository.findById(report.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + report.getCategory().getId())));
        }
        if (report.getReportDate() == null) {
            report.setReportDate(LocalDate.now());
        }
        return lostReportRepository.save(report);
    }

    public List<LostReport> getAllLostReports() {
        return lostReportRepository.findAll();
    }

    public List<LostReport> getLostReportsByCategory(Long categoryId) {
        return lostReportRepository.findByCategoryId(categoryId);
    }

    public List<FoundItem> findMatchesForLostReport(Long lostReportId) {
        LostReport lostReport = lostReportRepository.findById(lostReportId)
                .orElseThrow(() -> new IllegalArgumentException("Lost report not found with ID: " + lostReportId));
        return foundItemRepository.findMatchingFoundItems(lostReport.getCategory().getId(), lostReport.getDescription());
    }

    public FoundItem createFoundItem(FoundItem item) {
        if (item.getReporter() != null && item.getReporter().getId() != null) {
            item.setReporter(getUserById(item.getReporter().getId()));
        }
        if (item.getCategory() != null && item.getCategory().getId() != null) {
            item.setCategory(categoryRepository.findById(item.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + item.getCategory().getId())));
        }
        item.setStatus(ItemStatus.AVAILABLE);
        return foundItemRepository.save(item);
    }

    public List<FoundItem> getAllFoundItems() {
        return foundItemRepository.findAll();
    }

    public List<FoundItem> getFoundItemsByStatus(ItemStatus status) {
        return foundItemRepository.findByStatus(status);
    }

    public List<FoundItem> findMatches(Long categoryId, String keyword) {
        return foundItemRepository.findMatchingFoundItems(categoryId, keyword != null ? keyword.trim() : "");
    }

    public FoundItem updateItemStatus(Long itemId, ItemStatus newStatus, Long requesterUserId) {
        FoundItem item = foundItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Found item not found with ID: " + itemId));

        User requester = getUserById(requesterUserId);

        boolean isAdmin = "ADMIN".equalsIgnoreCase(requester.getRole());
        boolean isReporter = item.getReporter() != null && item.getReporter().getId().equals(requester.getId());

        if (!isAdmin && !isReporter) {
            throw new SecurityException("Unauthorized: Only an admin or the reporting user can modify this item's status.");
        }

        if (newStatus == ItemStatus.RETURNED && item.getStatus() != ItemStatus.CLAIMED) {
            throw new IllegalStateException("Invalid Transition: Item must be 'CLAIMED' before it can be marked 'RETURNED'.");
        }

        item.setStatus(newStatus);
        return foundItemRepository.save(item);
    }

    public Map<String, Object> getAdminDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalUsers", userRepository.count());
        metrics.put("totalCategories", categoryRepository.count());
        metrics.put("pendingLostReports", lostReportRepository.count());
        metrics.put("availableFoundItems", foundItemRepository.countByStatus(ItemStatus.AVAILABLE));
        metrics.put("claimedFoundItems", foundItemRepository.countByStatus(ItemStatus.CLAIMED));
        metrics.put("returnedFoundItems", foundItemRepository.countByStatus(ItemStatus.RETURNED));
        return metrics;
    }
}