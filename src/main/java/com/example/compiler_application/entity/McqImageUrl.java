package com.example.compiler_application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "mcq_image")
@Entity
public class McqImageUrl {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private MultipleChoiceQuestion multipleChoiceQuestion;

    private String imageUrl;

    @Override
    public String toString() {
        return "McqImageUrl{" +
                "Id='" + id + '\'' +
                ", multipleChoiceQuestion=" + multipleChoiceQuestion.getQuestionId() +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
