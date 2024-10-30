package com.rsuniverse.jobify_user.exception.customExceptions;

import com.rsuniverse.jobify_user.exception.CustomException;
import com.rsuniverse.jobify_user.models.enums.ErrorCode;
import lombok.Getter;

@Getter
public class JobSeekerException extends CustomException {
    protected JobSeekerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    protected JobSeekerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
