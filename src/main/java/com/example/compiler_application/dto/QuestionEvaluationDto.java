package com.example.compiler_application.dto;

import com.example.compiler_application.entity.CodingQuestionObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionEvaluationDto {
    private List<CodingQuestionObject> questionObjectList;
    private int totalMarks;
}
