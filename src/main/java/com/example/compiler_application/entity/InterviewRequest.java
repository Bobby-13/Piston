package com.example.compiler_application.entity;

import com.example.compiler_application.util.enums.InterviewRequestStatus;
import com.example.compiler_application.util.enums.InterviewRequestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interview_request")
public class InterviewRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String reason;

    @OneToOne
    private Interview interview;

    private LocalDateTime preferredTime;

    @Enumerated(EnumType.STRING)
    private InterviewRequestType interviewRequestType;

    @Enumerated(EnumType.STRING)
    private InterviewRequestStatus interviewRequestStatus;
}