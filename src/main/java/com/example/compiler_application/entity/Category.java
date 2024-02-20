package com.example.compiler_application.entity;

import com.example.compiler_application.util.enums.QuestionCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
@Data
@Builder
public class Category{

    @Id
    @GeneratedValue
    private int categoryId;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private QuestionCategory questionCategory;

}