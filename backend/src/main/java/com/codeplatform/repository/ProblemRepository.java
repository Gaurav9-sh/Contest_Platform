package com.codeplatform.repository;

import com.codeplatform.model.Problem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends MongoRepository<Problem, String> {
    List<Problem> findByContestId(String contestId);
}
