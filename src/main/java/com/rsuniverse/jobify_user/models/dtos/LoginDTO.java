package com.rsuniverse.jobify_user.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
public class LoginDTO {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Email(message = "invalid email format")
    @NotBlank(message = "username cannot be blank")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "password cannot be blank")
    private String password;

    private String accessToken;
    private String refreshToken;

}
