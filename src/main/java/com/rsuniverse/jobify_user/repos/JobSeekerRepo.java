package com.rsuniverse.jobify_user.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rsuniverse.jobify_user.models.entities.JobSeeker;

@Repository
public interface JobSeekerRepo extends  MongoRepository<JobSeeker, String>{
}
