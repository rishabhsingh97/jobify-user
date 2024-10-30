package com.rsuniverse.jobify_user.models.entities;

import com.mongodb.lang.NonNull;
import com.rsuniverse.jobify_user.models.enums.UserRole;
import com.rsuniverse.jobify_user.models.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("users")
public class User {

    @Id
    private String id;
    private String fullName;

    @NonNull
    @Indexed(unique = true)
    private String email;

    private String password;

    private String createdBy;
    private String updatedBy;
    private LocalDateTime lastLogin;
    private Set<UserRole> roles;
    private UserStatus status;

}
