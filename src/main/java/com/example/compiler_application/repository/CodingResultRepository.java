package com.example.compiler_application.repository;

import com.example.compiler_application.entity.CodingResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface CodingResultRepository extends MongoRepository<CodingResult,String> {
    CodingResult findByUserIdAndRoundId(String userId, String roundId);



    List<CodingResult> findByRoundId(String roundId);
}
