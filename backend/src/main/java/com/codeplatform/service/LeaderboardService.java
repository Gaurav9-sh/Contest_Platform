package com.codeplatform.service;

import com.codeplatform.dto.LeaderboardEntry;
import com.codeplatform.model.Submission;
import com.codeplatform.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final SubmissionRepository submissionRepository;

    public List<LeaderboardEntry> getLeaderboard(String contestId) {
        List<Submission> submissions = submissionRepository.findByContestId(contestId);

        Map<String, LeaderboardEntry> leaderboardMap = new HashMap<>();

        for (Submission submission : submissions) {
            String userId = submission.getUserId();

            leaderboardMap.putIfAbsent(userId, new LeaderboardEntry(
                    0,
                    userId,
                    submission.getUsername(),
                    0,
                    0,
                    null
            ));

            LeaderboardEntry entry = leaderboardMap.get(userId);

            if (submission.getStatus() == Submission.SubmissionStatus.ACCEPTED) {
                entry.setTotalScore(entry.getTotalScore() + submission.getScore());
                entry.setProblemsSolved(entry.getProblemsSolved() + 1);

                if (entry.getLastSubmissionTime() == null ||
                    submission.getCreatedAt().isAfter(entry.getLastSubmissionTime())) {
                    entry.setLastSubmissionTime(submission.getCreatedAt());
                }
            }
        }

        List<LeaderboardEntry> leaderboard = new ArrayList<>(leaderboardMap.values());

        leaderboard.sort((a, b) -> {
            if (b.getTotalScore() != a.getTotalScore()) {
                return Integer.compare(b.getTotalScore(), a.getTotalScore());
            }

            if (a.getLastSubmissionTime() == null && b.getLastSubmissionTime() == null) {
                return 0;
            }
            if (a.getLastSubmissionTime() == null) {
                return 1;
            }
            if (b.getLastSubmissionTime() == null) {
                return -1;
            }

            return a.getLastSubmissionTime().compareTo(b.getLastSubmissionTime());
        });

        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboard.get(i).setRank(i + 1);
        }

        return leaderboard;
    }
}
