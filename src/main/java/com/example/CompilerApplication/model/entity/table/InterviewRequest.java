package com.example.CompilerApplication.model.entity.table;


import com.example.CompilerApplication.model.entity.enums.InterviewRequestStatus;
import com.example.CompilerApplication.model.entity.enums.InterviewRequestType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    private Long id;

    private String reason;

    @OneToOne
    private Interview interview;

    private LocalDateTime preferredTime;

    private InterviewRequestType interviewRequest;

    private InterviewRequestStatus interviewRequestStatus;
}
