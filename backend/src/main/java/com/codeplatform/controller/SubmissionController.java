package com.codeplatform.controller;

import com.codeplatform.dto.SubmissionRequest;
import com.codeplatform.model.Submission;
import com.codeplatform.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<Submission> submitCode(@RequestBody SubmissionRequest request) {
        return ResponseEntity.ok(submissionService.submitCode(request));
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<Submission> getSubmission(@PathVariable String submissionId) {
        return ResponseEntity.ok(submissionService.getSubmission(submissionId));
    }
}
