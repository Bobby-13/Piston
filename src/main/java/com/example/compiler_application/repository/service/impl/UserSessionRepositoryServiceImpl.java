package com.example.compiler_application.repository.service.impl;

import com.example.compiler_application.entity.UserSession;
import com.example.compiler_application.repository.UserSessionRepository;
import com.example.compiler_application.repository.service.UserSessionRepositoryImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSessionRepositoryServiceImpl implements UserSessionRepositoryImplementation {

    private final UserSessionRepository repository;
    @Override
    public Optional<UserSession> findByUniqueId(String jit) {
        return repository.findById(jit);
    }
}
