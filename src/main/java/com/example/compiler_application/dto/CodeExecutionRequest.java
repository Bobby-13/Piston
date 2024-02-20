package com.example.compiler_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeExecutionRequest {
    private long questionId;
    private String code;
    private String language;
    private String version;
    private String input;
}
