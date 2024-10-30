package com.rsuniverse.jobify_user.exception.customExceptions;

import com.rsuniverse.jobify_user.exception.CustomException;
import com.rsuniverse.jobify_user.models.enums.ErrorCode;
import lombok.Getter;

@Getter
public class UserException extends CustomException {
    private ErrorCode errorCode;
    private String message;

    public UserException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

}

