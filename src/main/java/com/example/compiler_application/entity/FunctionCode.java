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
@Table(name = "function_code")
public class FunctionCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "function_code_id")
    private Long functionCodeId;

    @Enumerated(EnumType.STRING)
    private CodeLanguage codeLanguage;

    @Column(length = 1000)
    private String code;

    @ManyToOne
    @JsonIgnoreProperties("functionCodes")
    @JoinColumn(name = "question_id")
    private CodingQuestion codingQuestion;

    @Override
    public String toString() {
        return "FunctionCode{" +
                "functionCodeId=" + functionCodeId +
                ", codeLanguage=" + codeLanguage +
                ", code='" + code + '\'' +
                ", codingQuestion=" + codingQuestion.getQuestionId() +
                '}';
    }
}