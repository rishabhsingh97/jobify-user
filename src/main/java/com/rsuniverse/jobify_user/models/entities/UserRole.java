package com.rsuniverse.jobify_user.models.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "user_roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String role;

    @ElementCollection
    private List<String> authorities;
}
