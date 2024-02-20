package com.example.compiler_application.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Component
public class QuestionObject {

    private String qid;
    private String percentage;
    private String language;
    private Set<CasesObject> testcase;
    private String code;

}