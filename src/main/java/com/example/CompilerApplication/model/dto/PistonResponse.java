package com.example.CompilerApplication.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PistonResponse {

    private Run run;
    private String language;
    private String version;
}
