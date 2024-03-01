package com.example.compiler_application.entity;


import com.example.compiler_application.util.enums.Difficulty;
import com.example.compiler_application.util.enums.Result;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "codingResult")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodingResult {

    @Id
    private String id;
    private String contestId;
    private String roundId;
    private String userId;
    private List<CodingQuestionObject> question;
    private double totalScore;
    private Map<Difficulty,Integer> percentage;
    private Result result;
}
