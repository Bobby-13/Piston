package com.example.CompilerApplication.repository.service;

import com.example.CompilerApplication.model.entity.table.CodingResult;
import com.example.CompilerApplication.repository.CodingResultRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodingResultRepositoryImplementation{
    CodingResult findByUserIdAndRoundId(String userId, String roundId);

//    List<?> findByUserIdAndRoundIdOrderByQuestionId(String userId, String roundId, String questionId);
}
