package com.example.compiler_application.repository;

import com.example.compiler_application.entity.CodingResult;
import com.example.compiler_application.entity.Rounds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoundsRepository extends JpaRepository<Rounds,String> {

    @Query("SELECT r.pass FROM Rounds r WHERE r.id = ?1")
    int getPassMark(String roundId);


}
