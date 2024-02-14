package com.example.CompilerApplication.repository.service.Implementation;

import com.example.CompilerApplication.model.entity.table.CodingResult;
import com.example.CompilerApplication.repository.CodingResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodingResultRepositoryServiceImpl implements CodingResultRepositoryImplementation{

    @Autowired
    private CodingResultRepository codingResultRepository;
    @Override
    public CodingResult findByUserIdAndRoundId(String userId, String roundId) {
        return codingResultRepository.findByUserIdAndRoundId(userId, roundId);
    }
}
