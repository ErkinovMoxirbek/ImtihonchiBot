package com.example.dto;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class TestDTO {
    Integer examId;
    private Integer id;
    private String question;
    private String AOption;
    private String BOption;
    private String COption;
    private String DOption;
    private String answer;
    private Double grade;
}
