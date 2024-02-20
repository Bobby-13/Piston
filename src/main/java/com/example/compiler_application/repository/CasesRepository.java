package com.example.compiler_application.repository;

import com.example.compiler_application.entity.Cases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CasesRepository extends JpaRepository<Cases,Long> {
}
