package com.rsuniverse.jobify_user.repos;

import com.rsuniverse.jobify_user.models.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepo extends JpaRepository<Candidate, String> {
}
