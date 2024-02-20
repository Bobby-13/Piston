package com.example.compiler_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PistonRequest {
    private String language;
    private String version;
    private List<FileDetails> files;
    private String stdin;
    private List<String> args;
    private int compileTimeout;
    private int runTimeout;
    private int compileMemoryLimit;
    private int runMemoryLimit;

}
