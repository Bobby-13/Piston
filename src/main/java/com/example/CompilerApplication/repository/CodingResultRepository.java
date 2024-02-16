package com.example.CompilerApplication.repository;

import com.example.CompilerApplication.model.entity.table.CodingQuestion;
import com.example.CompilerApplication.model.entity.table.CodingResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface CodingResultRepository extends MongoRepository<CodingResult,String> {
    CodingResult findByUserIdAndRoundId(String userId, String roundId);

//    Optional<CodingQuestion> findByRoundIdAndUserIdAndOrderByQuestionId(String roundId, String userId, Long questionId);
}
