package com.example.CompilerApplication.model.dto;

import com.example.CompilerApplication.model.entity.enums.CodeLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaticCodeDto {
    private long staticCodeId;
    private CodeLanguage codeLanguage;
    private String code;
}
