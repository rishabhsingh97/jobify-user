package com.rsuniverse.jobify_user.exception.customExceptions;

import com.rsuniverse.jobify_user.exception.CustomException;
import com.rsuniverse.jobify_user.models.enums.ErrorCode;
import lombok.Getter;

@Getter
public class JobSeekerException extends CustomException {

    private final ErrorCode errorCode;
    private final String message;

    protected JobSeekerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.errorCode = errorCode;
        this.message = message;
    }

    protected JobSeekerException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
