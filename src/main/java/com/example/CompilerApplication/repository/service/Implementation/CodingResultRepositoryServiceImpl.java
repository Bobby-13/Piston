package com.example.CompilerApplication.repository.service.Implementation;

import com.example.CompilerApplication.model.entity.table.CodingResult;
import com.example.CompilerApplication.repository.CodingResultRepository;
import com.example.CompilerApplication.repository.service.CodingResultRepositoryImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodingResultRepositoryServiceImpl implements CodingResultRepositoryImplementation {

    @Autowired
    private CodingResultRepository codingResultRepository;
    @Override
    public CodingResult findByUserIdAndRoundId(String userId, String roundId) {
        return codingResultRepository.findByUserIdAndRoundId(userId, roundId);
    }

//    @Override
//    public List<?> findByUserIdAndRoundIdOrderByQuestionId(String userId, String roundId, String questionId) {
//        return codingResultRepository.findByUserIdAndRoundIdOrderByQuestionId(userId,roundId,questionId);
//    }
}
