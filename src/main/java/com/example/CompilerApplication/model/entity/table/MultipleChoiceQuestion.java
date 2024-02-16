package com.example.CompilerApplication.model.entity.table;

import com.example.CompilerApplication.model.entity.enums.Difficulty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "mcq_question")
public class MultipleChoiceQuestion {

    @Id
    private String questionId;

    @OneToMany(mappedBy = "multipleChoiceQuestion", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("multipleChoiceQuestion")
    private List<McqImageUrl> imageUrl;

    @Column(length = 3000)
    private String question;

    @OneToMany(mappedBy = "multipleChoiceQuestion", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("multipleChoiceQuestion")
    private List<Options> options;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @Override
    public String toString() {
        return "MultipleChoiceQuestion{" +
                "question_id='" + questionId + '\'' +
                ", imageUrl=" + (imageUrl == null ? null : imageUrl.toString()) +
                ", question='" + question + '\'' +
                ", options=" + (options == null ? null : options.toString()) +
                ", difficulty=" + difficulty +
                ", category=" + category +
                '}';
    }
}
