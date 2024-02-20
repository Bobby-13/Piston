package com.example.compiler_application.repository;

import com.example.compiler_application.entity.CodingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodingQuestionRepository extends JpaRepository<CodingQuestion,Long> {
}
