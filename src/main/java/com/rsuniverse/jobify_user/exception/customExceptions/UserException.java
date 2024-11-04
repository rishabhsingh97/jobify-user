package com.rsuniverse.jobify_user.exception.customExceptions;

import com.rsuniverse.jobify_user.exception.CustomException;
import com.rsuniverse.jobify_user.models.enums.ErrorCode;
import lombok.Getter;

@Getter
public class UserException extends CustomException {
    private final ErrorCode errorCode;
    private final String message;

    public UserException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public UserException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

}

