package com.java.campus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // <-- Must be jakarta.validation.constraints
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "lost_reports")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class LostReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Report date is required")
    private LocalDate reportDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}