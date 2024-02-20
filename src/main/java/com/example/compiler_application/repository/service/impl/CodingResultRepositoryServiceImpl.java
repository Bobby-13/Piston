package com.example.compiler_application.repository.service.impl;

import com.example.compiler_application.entity.CodingResult;
import com.example.compiler_application.repository.CodingResultRepository;
import com.example.compiler_application.repository.service.CodingResultRepositoryImplementation;
import org.springframework.stereotype.Service;

@Service
public class CodingResultRepositoryServiceImpl implements CodingResultRepositoryImplementation {


    private final CodingResultRepository codingResultRepository;

    public CodingResultRepositoryServiceImpl(CodingResultRepository codingResultRepository) {
        this.codingResultRepository = codingResultRepository;
    }

    @Override
    public CodingResult findByUserIdAndRoundId(String userId, String roundId) {
        return codingResultRepository.findByUserIdAndRoundId(userId, roundId);
    }
}
