package com.example.CompilerApplication.model.entity.table;


import lombok.*;


@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CasesObject {

    private String testcase_id;
    private String input;
    private String output;
    private String user_output;
}