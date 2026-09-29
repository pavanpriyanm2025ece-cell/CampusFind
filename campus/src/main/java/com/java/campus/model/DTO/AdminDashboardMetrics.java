package com.java.campus.model.DTO;

public class AdminDashboardMetrics {
    private long totalUsers;
    private long totalCategories;
    private long totalLostReports;
    private long totalFoundItems;

    public AdminDashboardMetrics() {}

    public AdminDashboardMetrics(long totalUsers, long totalCategories, long totalLostReports, long totalFoundItems) {
        this.totalUsers = totalUsers;
        this.totalCategories = totalCategories;
        this.totalLostReports = totalLostReports;
        this.totalFoundItems = totalFoundItems;
    }

    // Getters and Setters
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalCategories() { return totalCategories; }
    public void setTotalCategories(long totalCategories) { this.totalCategories = totalCategories; }

    public long getTotalLostReports() { return totalLostReports; }
    public void setTotalLostReports(long totalLostReports) { this.totalLostReports = totalLostReports; }

    public long getTotalFoundItems() { return totalFoundItems; }
    public void setTotalFoundItems(long totalFoundItems) { this.totalFoundItems = totalFoundItems; }
}