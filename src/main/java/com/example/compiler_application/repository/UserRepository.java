package com.example.compiler_application.repository;

import com.example.compiler_application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {

    User findByUserId(String userId);
}
