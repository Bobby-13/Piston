package com.example.CompilerApplication.model.entity.table;


import com.example.CompilerApplication.model.entity.enums.Category;
import com.example.CompilerApplication.model.entity.enums.Difficulty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "coding_question")
public class CodingQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "question_id")
    private Long questionId;

    @Column(length = 1000)
    private String question;

    @Column(length = 1000)
    private String output;

    @OneToMany
    @JoinColumn(name = "Id")
    private List<CodingImageUrl> imageUrl;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "codingQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("codingQuestion")
    private List<Cases> casesList;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
}
