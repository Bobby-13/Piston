package com.example.CompilerApplication.model.entity.table;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ContestAndMcq implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private MultipleChoiceQuestion multipleChoiceQuestion;
}
