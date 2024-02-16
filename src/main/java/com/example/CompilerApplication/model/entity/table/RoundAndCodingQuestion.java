package com.example.CompilerApplication.model.entity.table;


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
