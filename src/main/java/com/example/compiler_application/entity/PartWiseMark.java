package com.example.compiler_application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartWiseMark {

    private String part;
    private Map<String, Double> difficultyWiseMarks;
    private int correctAnswerCount;
}
