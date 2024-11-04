package com.rsuniverse.jobify_user.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="admins")
public class Admin extends User {

    @Column(unique = true, nullable = false)
    private String adminId = UUID.randomUUID().toString();

    private String userId;

}
