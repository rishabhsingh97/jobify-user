package com.rsuniverse.jobify_user.repos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rsuniverse.jobify_user.models.entities.User;

@Repository
public interface UserRepo extends MongoRepository<User, String> {

    Optional<User> findByEmail(String username);
    boolean existsUserByEmail(String email);

}
