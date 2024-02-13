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
public class HiddenTestCaseDto {

    private String id;
    private Result result;
}
