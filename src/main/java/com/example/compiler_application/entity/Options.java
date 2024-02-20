package com.example.compiler_application.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "option")
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private MultipleChoiceQuestion multipleChoiceQuestion;

    private String option;

    private boolean isCorrect;
    @Override
    public String toString() {
        return "Options{" +
                "id=" + id +
                ", multipleChoiceQuestion=" + multipleChoiceQuestion.getQuestionId() +
                ", option='" + option + '\'' +
                '}';
    }

}
