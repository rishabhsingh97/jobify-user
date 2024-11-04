package com.rsuniverse.jobify_user.models.entities;

import com.rsuniverse.jobify_user.models.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recruiter extends User {

    @Column(unique = true, nullable = false)
    private String recruiterId = UUID.randomUUID().toString();

    @OneToMany
    private List<UserRole> roles;

    private UserStatus status;


}
