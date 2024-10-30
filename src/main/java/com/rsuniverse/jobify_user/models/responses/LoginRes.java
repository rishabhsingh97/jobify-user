package com.rsuniverse.jobify_user.models.responses;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRes {

    @Email(message="Invalid email format")
    @NotBlank(message="Email can not be blank")
    String email;
    
    @NotBlank(message="Password can not be blank")
    String password;
}
