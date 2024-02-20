package com.example.compiler_application.entity;


import com.example.compiler_application.util.enums.EmployeeType;
import com.example.compiler_application.util.enums.Role;
import com.example.compiler_application.util.enums.Stack;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "employee")
public class Employee {

    @Id
    private Long employeeId;

    @Column(unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "contest_employee",
            joinColumns = {
                    @JoinColumn(name = "employee_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "contest_id")
            }
    )
    private Set<Contest> contest;

    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Interview> interviews;

    @Enumerated(EnumType.STRING)
    private Stack stack;

    private int yearsOfExperience;

}
