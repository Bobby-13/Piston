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
@Table(name = "round_and_mcq_question")
public class RoundAndMcqQuestion {

    @EmbeddedId
    private ContestAndMcq contestAndMcq;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "round_id")
    private Rounds rounds;

}
