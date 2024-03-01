package com.example.compiler_application.dto;

import com.example.compiler_application.entity.TestCasesObject;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TestCaseEvaluationDto {
  private List<TestCasesObject> testCases;
  private int passCount;
  private int testCaseCount;
  private int passPercentage;
}
