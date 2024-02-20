package com.example.compiler_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationRequestDto {
    private List<String > sampleInput;
    private List<String> hiddenInput;
    private List<String> sampleOutput;
    private List<String> hiddenOutput;
    private List<StaticCodeDto> staticCodeDto;
}
