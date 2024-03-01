package com.example.compiler_application.repository.service;

import com.example.compiler_application.entity.User;

public interface UserRepositoryImplementation {
    User getUserById(String userId);

    void updateUserByResult(User user);
}
