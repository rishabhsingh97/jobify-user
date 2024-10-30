package com.rsuniverse.jobify_user.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshDTO {

    @NotBlank(message = "refresh token cannot be empty")
    private String refreshToken;
    private String accessToken;
}
