package com.example.compiler_application.entity;

import com.example.compiler_application.util.enums.Difficulty;
import com.example.compiler_application.util.enums.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodingQuestionObject {
    private long questionId;
    private String code;
    private String language;
    private Difficulty difficulty;
    private QuestionCategory questionCategory;
    private List<TestCasesObject> testCases;
    private int passCount;
    private int testCaseCount;
    private int score;
}
