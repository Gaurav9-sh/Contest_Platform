package com.codeplatform.repository;

import com.codeplatform.model.Contest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends MongoRepository<Contest, String> {
}
