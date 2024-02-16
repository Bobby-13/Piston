package com.example.CompilerApplication.model.entity.table;


import com.example.CompilerApplication.model.entity.enums.InterviewResult;
import com.example.CompilerApplication.model.entity.enums.InterviewType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "interview")
@Data
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String interviewId;

    @OneToOne
    @JoinColumn(name = "userId")
    @JsonIgnoreProperties("contest")
    private User user;

    @ManyToOne
    @JoinColumn(name = "employeeId")
    @JsonIgnoreProperties("contest")
    private Employee employee;

    @Enumerated(EnumType.STRING)
    private InterviewType interviewType;

    private String feedBack;

    @Enumerated(EnumType.STRING)
    private InterviewResult interviewResult;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "roundsId")
    private Rounds rounds;

    private LocalDateTime interviewTime;
}

