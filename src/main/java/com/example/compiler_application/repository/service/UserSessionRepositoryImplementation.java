package com.example.compiler_application.repository.service;

import com.example.compiler_application.entity.UserSession;

import java.util.Optional;

public interface UserSessionRepositoryImplementation {
    Optional<UserSession> findByUniqueId(String jit);
}
