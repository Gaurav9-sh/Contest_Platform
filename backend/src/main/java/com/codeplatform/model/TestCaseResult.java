package com.codeplatform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResult {
    private int testCaseNumber;
    private boolean passed;
    private String actualOutput;
    private String expectedOutput;
    private String error;
}
