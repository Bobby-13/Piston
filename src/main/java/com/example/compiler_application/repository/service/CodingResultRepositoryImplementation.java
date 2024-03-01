package com.example.compiler_application.repository.service;

import com.example.compiler_application.entity.CodingResult;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodingResultRepositoryImplementation{
    CodingResult findByUserIdAndRoundId(String userId, String roundId);

    List<CodingResult> findByRoundId(String roundId);
}
