package com.rsuniverse.jobify_user.models.entities;

import com.mongodb.lang.NonNull;
import com.rsuniverse.jobify_user.models.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("job_seekers")
public class JobSeeker {

    @Id
    private String id;
    private String userId;
    private String firstName;
    private String lastName;

    @NonNull
    @Indexed(unique = true)
    private String email;

    private String textResume;
    private UserStatus status;

}
