package com.example.compiler_application.repository.service;

import com.example.compiler_application.entity.CodingResult;
import org.springframework.stereotype.Repository;

@Repository
public interface CodingResultRepositoryImplementation{
    CodingResult findByUserIdAndRoundId(String userId, String roundId);
}
