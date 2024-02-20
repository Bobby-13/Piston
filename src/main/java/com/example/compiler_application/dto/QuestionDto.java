package com.example.compiler_application.dto;

import com.example.compiler_application.entity.CodingImageUrl;
import com.example.compiler_application.util.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDto {
        private long questionId;
        private String question;
        private List<CodingImageUrl> imageUrl;
        private String category;
        private List<CaseDto> casesList;
        private Difficulty difficulty;
        private List<FunctionCodeDto> functionCodes;

    }
