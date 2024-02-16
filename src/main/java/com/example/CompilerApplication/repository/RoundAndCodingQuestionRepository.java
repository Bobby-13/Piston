package com.example.CompilerApplication.repository;

import com.example.CompilerApplication.model.entity.table.CodingQuestion;
import com.example.CompilerApplication.model.entity.table.RoundAndCodingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundAndCodingQuestionRepository extends JpaRepository<RoundAndCodingQuestion,String> {

    @Query("SELECT q.contestAndCoding.codingQuestion FROM RoundAndCodingQuestion q WHERE q.rounds.id =?1")
    List<CodingQuestion> getCodingQuestion(String roundId);

    @Query("SELECT q.contestAndCoding.codingQuestion FROM RoundAndCodingQuestion q WHERE q.rounds.id =?1 AND q.contestAndCoding.codingQuestion.questionId =?2")
    CodingQuestion getQuestionById(String roundId, long questionId);
}
