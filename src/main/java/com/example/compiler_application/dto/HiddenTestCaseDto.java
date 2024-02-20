package com.example.compiler_application.dto;

import com.example.compiler_application.util.enums.Result;
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
