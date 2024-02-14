package com.example.CompilerApplication.model.entity.table;

import com.example.CompilerApplication.model.entity.enums.QuestionCategory;
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
    private QuestionCategory category;

}