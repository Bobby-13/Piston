package com.example.compiler_application.repository.service;

import com.example.compiler_application.entity.CodingQuestion;

import java.util.List;

public interface RoundAndCodingQuestionRepositoryImplementation {
    List<CodingQuestion> getCodingQuestion(String roundId);
    CodingQuestion getStaticCodeAndTestCases(String roundId, long questionId);
}
