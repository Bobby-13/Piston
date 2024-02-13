package com.example.CompilerApplication.repository;

import com.example.CompilerApplication.model.entity.table.Cases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CasesRepository extends JpaRepository<Cases,Long> {
}
