package com.rsuniverse.jobify_user.exception.customExceptions;

import com.rsuniverse.jobify_user.exception.CustomException;
import com.rsuniverse.jobify_user.models.enums.ErrorCode;
import lombok.Getter;

@Getter
public class AuthException extends CustomException {
    public AuthException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
