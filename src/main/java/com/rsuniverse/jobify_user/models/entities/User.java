package com.rsuniverse.jobify_user.models.entities;

import com.rsuniverse.jobify_user.models.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String fullName;
    private String email;
    private String password;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime lastLogin;

    @OneToMany
    private List<UserRole> roles;

    private UserStatus status;

}

