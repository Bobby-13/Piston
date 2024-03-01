package com.example.compiler_application.repository.service.impl;

import com.example.compiler_application.entity.User;
import com.example.compiler_application.repository.UserRepository;
import com.example.compiler_application.repository.service.UserRepositoryImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRepositoryServiceImpl implements UserRepositoryImplementation {

    private  final UserRepository userRepository;
    @Override
    public User getUserById(String userId) {
       return userRepository.findByUserId(userId);
    }

    @Override
    public void updateUserByResult(User user) {
       userRepository.save(user);
    }
}
