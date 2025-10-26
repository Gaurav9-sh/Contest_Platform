package com.codeplatform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "submissions")
public class Submission {
    @Id
    private String id;
    private String userId;
    private String username;
    private String contestId;
    private String problemId;
    private String code;
    private String language;
    private SubmissionStatus status;
    private String result;
    private int score;
    private int totalTestCases;
    private int passedTestCases;
    private List<TestCaseResult> testCaseResults;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public enum SubmissionStatus {
        PENDING, RUNNING, ACCEPTED, WRONG_ANSWER, ERROR, TIME_LIMIT_EXCEEDED, COMPILATION_ERROR
    }
}
