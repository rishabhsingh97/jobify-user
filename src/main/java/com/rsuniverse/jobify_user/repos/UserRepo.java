package com.rsuniverse.jobify_user.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rsuniverse.jobify_user.models.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    Optional<User> findByEmail(String username);
    boolean existsUserByEmail(String email);

}
