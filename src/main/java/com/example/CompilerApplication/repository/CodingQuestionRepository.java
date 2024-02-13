package com.example.CompilerApplication.repository;

import com.example.CompilerApplication.model.entity.table.CodingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodingQuestionRepository extends JpaRepository<CodingQuestion,Long> {
}
