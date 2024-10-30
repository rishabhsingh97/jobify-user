package com.rsuniverse.jobify_user.models.dtos;

import com.rsuniverse.jobify_user.models.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobSeekerDTO implements Serializable {

    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String textResume;
    private UserStatus status;

}
