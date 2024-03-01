package com.example.compiler_application.repository.service.impl;

import com.example.compiler_application.entity.CodingResult;
import com.example.compiler_application.repository.RoundsRepository;
import com.example.compiler_application.repository.service.RoundsRepositoryImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundsRepositoryServiceImpl implements RoundsRepositoryImplementation {

    private final RoundsRepository repository;


    @Override
    public int getPassMark(String roundId) {
        return repository.getPassMark(roundId);

    }

}
