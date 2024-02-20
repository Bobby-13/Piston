package com.example.compiler_application.repository.service.impl;

import com.example.compiler_application.entity.CodingQuestion;
import com.example.compiler_application.repository.RoundAndCodingQuestionRepository;
import com.example.compiler_application.repository.service.RoundAndCodingQuestionRepositoryImplementation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoundAndCodingQuestionImpl implements RoundAndCodingQuestionRepositoryImplementation {


    private final RoundAndCodingQuestionRepository repository;

    public RoundAndCodingQuestionImpl(RoundAndCodingQuestionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CodingQuestion> getCodingQuestion(String roundId) {
        return repository.getCodingQuestion(roundId);
    }

    @Override
    public CodingQuestion getStaticCodeAndTestCases(String roundId, long questionId) {
           return repository.getQuestionById(roundId,questionId);
    }
}
