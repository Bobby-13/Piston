package com.example.CompilerApplication.repository;

import com.example.CompilerApplication.model.entity.table.CodingResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;

public interface CodingResultRepository extends MongoRepository<CodingResult,String> {

    CodingResult findByUserIdAndRoundId(String userId, String roundId);
}
