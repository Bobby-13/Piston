package com.example.CompilerApplication.repository.service.Implementation;

import com.example.CompilerApplication.model.entity.table.CodingQuestion;
import com.example.CompilerApplication.repository.RoundAndCodingQuestionRepository;
import com.example.CompilerApplication.repository.service.RoundAndCodingQuestionRepositoryImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoundAndCodingQuestionImpl implements RoundAndCodingQuestionRepositoryImplementation {

    @Autowired
    private RoundAndCodingQuestionRepository repository;
    @Override
    public List<CodingQuestion> getCodingQuestion(String roundId) {
        return repository.getCodingQuestion(roundId);
    }

    @Override
    public CodingQuestion getStaticCodeAndTestCases(String roundId, long questionId) {
           return repository.getQuestionById(roundId,questionId);
    }
}
