package com.example.compiler_application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Table(name = "part")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private int easy;
    private int medium;
    private int hard;

    @ManyToOne
    @JoinColumn(name = "rounds_id")
    private Rounds rounds;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private int assignedTime;
}
