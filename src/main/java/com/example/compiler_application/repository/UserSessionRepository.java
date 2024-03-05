package com.example.compiler_application.repository;

import com.example.compiler_application.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession,String> {
}
