package com.example.compiler_application.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class RoundAndCodingQuestion {

    @EmbeddedId
    private ContestAndCoding contestAndCoding;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Rounds rounds;

}
