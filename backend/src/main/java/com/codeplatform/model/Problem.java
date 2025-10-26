package com.codeplatform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "problems")
public class Problem {
    @Id
    private String id;
    private String contestId;
    private String title;
    private String description;
    private int marks;
    private String difficulty;
    private List<TestCase> testCases;
}
