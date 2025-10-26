package com.codeplatform.service;

import com.codeplatform.dto.ContestResponse;
import com.codeplatform.model.Contest;
import com.codeplatform.model.Problem;
import com.codeplatform.repository.ContestRepository;
import com.codeplatform.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContestService {

    private final ContestRepository contestRepository;
    private final ProblemRepository problemRepository;

    public ContestResponse getContestWithProblems(String contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest not found"));

        List<Problem> problems = problemRepository.findByContestId(contestId);

        problems.forEach(problem -> {
            problem.getTestCases().removeIf(tc -> !tc.isVisible());
        });

        return new ContestResponse(contest, problems);
    }

    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }
}
