package com.example.CompilerApplication.model.entity.table;

import com.example.CompilerApplication.model.entity.enums.Result;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "mcqResult")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MCQResult {

    @Id
    private String mcqResultId;
    private String contestId;
    private String roundId;
    private String userId;

    @Enumerated(EnumType.STRING)
    private Result result;

    private List<PartWiseResponse> savedMcq;
    private List<PartWiseMark> partWiseMarks;
    private int totalMarks;
    private float totalPercentage;

}
