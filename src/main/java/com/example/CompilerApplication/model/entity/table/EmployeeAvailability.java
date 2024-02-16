package com.example.CompilerApplication.model.entity.table;


import com.example.CompilerApplication.model.entity.enums.EmployeeResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "employee_availability")
@Entity
public class EmployeeAvailability{

    @EmbeddedId
    private EmployeeAndContest employeeAndContest;

    @Enumerated(EnumType.STRING)
    private EmployeeResponse response;
}
