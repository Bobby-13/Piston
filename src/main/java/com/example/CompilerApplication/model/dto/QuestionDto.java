package com.example.CompilerApplication.model.dto;

import com.example.CompilerApplication.model.entity.enums.Difficulty;
import com.example.CompilerApplication.model.entity.table.Category;
import com.example.CompilerApplication.model.entity.table.CodingImageUrl;
import com.example.CompilerApplication.model.entity.table.FunctionCode;
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
