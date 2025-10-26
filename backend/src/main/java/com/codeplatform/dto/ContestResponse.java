package com.codeplatform.dto;

import com.codeplatform.model.Contest;
import com.codeplatform.model.Problem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestResponse {
    private Contest contest;
    private List<Problem> problems;
}
