package com.example.CompilerApplication.model.dto;

import com.example.CompilerApplication.model.entity.enums.CodeLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodingQuestionDto {
    private long questionId;
    private String code;
    private String language;
    private List<TestCasesDto> testCases;
    private double score;
}
