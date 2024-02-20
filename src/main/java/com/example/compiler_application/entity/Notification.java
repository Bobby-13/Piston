package com.example.compiler_application.entity;


import com.example.compiler_application.util.enums.JobType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Notification {

    @Id
    private Long id;

    private String jobId;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}
