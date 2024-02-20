package com.example.compiler_application.dto;

import com.example.compiler_application.util.enums.CodeLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FunctionCodeDto {
    private long functionCodeId;
    private CodeLanguage codeLanguage;
    private String code;
}
