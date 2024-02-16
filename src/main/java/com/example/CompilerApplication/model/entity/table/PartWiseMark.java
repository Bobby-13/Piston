package com.example.CompilerApplication.model.entity.table;

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
