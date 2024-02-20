package com.example.compiler_application.entity;

import com.example.compiler_application.util.enums.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCasesObject {
    private String id;
    private String input;
    private String output;
    private String expectedOutput;
    private Result result;
}
