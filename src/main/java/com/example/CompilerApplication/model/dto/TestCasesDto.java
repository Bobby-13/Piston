package com.example.CompilerApplication.model.dto;

import com.example.CompilerApplication.model.entity.enums.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCasesDto {
    private String id;
    private String input;
    private String output;
    private String expectedOutput;
    private Result result;
}
