package com.rsuniverse.jobify_user.models.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="candidates")
public class Candidate extends User {

    @Column(unique = true, nullable = false)
    private String candidateId = UUID.randomUUID().toString();
    private String userId;
    private String bio;
    private String textResume;

}
