package com.example.CompilerApplication.repository.service.Implementation;

import com.example.CompilerApplication.model.entity.table.CodingResult;
import com.example.CompilerApplication.repository.CodingResultRepository;

public interface CodingResultRepositoryImplementation{

    CodingResult findByUserIdAndRoundId(String userId, String roundId);
}
