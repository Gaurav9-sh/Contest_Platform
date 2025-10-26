package com.codeplatform.controller;

import com.codeplatform.dto.ContestResponse;
import com.codeplatform.dto.LeaderboardEntry;
import com.codeplatform.service.ContestService;
import com.codeplatform.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contests")
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;
    private final LeaderboardService leaderboardService;

    @GetMapping("/{contestId}")
    public ResponseEntity<ContestResponse> getContest(@PathVariable String contestId) {
        return ResponseEntity.ok(contestService.getContestWithProblems(contestId));
    }

    @GetMapping("/{contestId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard(@PathVariable String contestId) {
        return ResponseEntity.ok(leaderboardService.getLeaderboard(contestId));
    }
}
