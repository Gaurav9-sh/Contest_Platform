package com.codeplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntry {
    private int rank;
    private String userId;
    private String username;
    private int totalScore;
    private int problemsSolved;
    private LocalDateTime lastSubmissionTime;
}
