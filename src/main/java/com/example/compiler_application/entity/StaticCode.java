package com.example.compiler_application.entity;

import com.example.compiler_application.util.enums.CodeLanguage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "static_code")
public class StaticCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "static_code_id")
    private Long staticCodeId;

    @Enumerated(EnumType.STRING)
    private CodeLanguage codeLanguage;

    @Column(length = 1000)
    private String code;

    @ManyToOne
    @JsonIgnoreProperties("staticCodes")
    @JoinColumn(name = "question_id")
    private CodingQuestion codingQuestion;

    @Override
    public String toString() {
        return "StaticCode{" +
                "staticCodeId=" + staticCodeId +
                ", codeLanguage=" + codeLanguage +
                ", code='" + code + '\'' +
                ", codingQuestion=" + codingQuestion.getQuestionId() +
                '}';
    }
}