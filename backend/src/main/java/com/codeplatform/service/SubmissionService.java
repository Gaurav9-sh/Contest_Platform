package com.codeplatform.service;

import com.codeplatform.dto.SubmissionRequest;
import com.codeplatform.model.Problem;
import com.codeplatform.model.Submission;
import com.codeplatform.repository.ProblemRepository;
import com.codeplatform.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final JudgeService judgeService;

    public Submission submitCode(SubmissionRequest request) {
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        Submission submission = new Submission();
        submission.setUserId(request.getUserId());
        submission.setUsername(request.getUsername());
        submission.setContestId(request.getContestId());
        submission.setProblemId(request.getProblemId());
        submission.setCode(request.getCode());
        submission.setLanguage(request.getLanguage());
        submission.setStatus(Submission.SubmissionStatus.PENDING);
        submission.setScore(0);
        submission.setTotalTestCases(problem.getTestCases().size());
        submission.setPassedTestCases(0);
        submission.setTestCaseResults(new ArrayList<>());
        submission.setCreatedAt(LocalDateTime.now());

        submission = submissionRepository.save(submission);

        judgeService.judgeSubmission(submission.getId());

        return submission;
    }

    public Submission getSubmission(String submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }
}
