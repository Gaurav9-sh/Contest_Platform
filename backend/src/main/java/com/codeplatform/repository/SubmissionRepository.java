package com.codeplatform.repository;

import com.codeplatform.model.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends MongoRepository<Submission, String> {
    List<Submission> findByContestId(String contestId);
    List<Submission> findByContestIdAndStatus(String contestId, Submission.SubmissionStatus status);
    List<Submission> findByUserIdAndContestId(String userId, String contestId);
}
