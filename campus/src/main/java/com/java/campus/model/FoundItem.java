package com.java.campus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "found_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoundItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String location;
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reporter;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    // Explicit Getters and Setters
    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }
}