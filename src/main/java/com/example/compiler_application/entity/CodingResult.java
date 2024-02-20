package com.example.compiler_application.entity;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
    private double totalMarks;
}
