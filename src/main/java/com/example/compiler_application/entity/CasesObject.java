package com.example.compiler_application.entity;


import lombok.*;


@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CasesObject {

    private String testcaseId;
    private String input;
    private String output;
    private String userOutput;
}