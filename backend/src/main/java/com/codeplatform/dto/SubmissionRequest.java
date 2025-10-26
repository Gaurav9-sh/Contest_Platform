package com.codeplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionRequest {
    private String userId;
    private String username;
    private String contestId;
    private String problemId;
    private String code;
    private String language;
}
