package com.example.CompilerApplication.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Run {
    private String stdout;
    private String stderr;
    private int code;
    private String signal;
    private String output;
}
