package com.example.CompilerApplication.model.entity.table;


import com.example.CompilerApplication.model.dto.CodingQuestionDto;
import com.example.CompilerApplication.model.dto.TestCasesDto;
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
    private List<CodingQuestionDto> question;
    private double totalMarks;
}
