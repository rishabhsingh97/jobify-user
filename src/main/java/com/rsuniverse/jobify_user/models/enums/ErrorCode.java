package com.rsuniverse.jobify_user.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    AUTH_INVALID_USERNAME(201, "invalid username", HttpStatus.FORBIDDEN),
    AUTH_INVALID_PASSWORD(202, "invalid password", HttpStatus.FORBIDDEN),
    AUTH_TOKEN_EXPIRED(203, "invalid password", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_NOT_REFRESHED(204, "invalid password", HttpStatus.UNAUTHORIZED),

    USER_NOT_FOUND(301,"user not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(302,"user already exists", HttpStatus.CONFLICT),;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
