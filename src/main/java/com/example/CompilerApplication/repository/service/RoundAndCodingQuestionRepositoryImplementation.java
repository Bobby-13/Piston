package com.example.CompilerApplication.repository.service;

import com.example.CompilerApplication.model.entity.table.CodingQuestion;

import java.util.List;

public interface RoundAndCodingQuestionRepositoryImplementation {
    List<CodingQuestion> getCodingQuestion(String roundId);

    CodingQuestion getStaticCodeAndTestCases(String roundId, long questionId);
}
