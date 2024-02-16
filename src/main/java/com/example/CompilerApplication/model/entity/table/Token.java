package com.example.CompilerApplication.model.entity.table;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
//@Entity
@Data
//@Table(name = "token_table")
public class Token {

    @Id
    private Long id;

    private String userId;
    private String token;
    private boolean revoked;
    private boolean expired;
}
