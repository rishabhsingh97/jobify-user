package com.rsuniverse.jobify_user.exception;

import com.rsuniverse.jobify_user.exception.customExceptions.AuthException;
import com.rsuniverse.jobify_user.exception.customExceptions.JobSeekerException;
import com.rsuniverse.jobify_user.exception.customExceptions.UserException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.rsuniverse.jobify_user.models.responses.BaseRes;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<BaseRes<String>> handleValidationExceptions(Exception ex) {
        String errorMessage = switch (ex) {
            case ConstraintViolationException violationException ->
                    violationException.getConstraintViolations()
                            .stream()
                            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                            .collect(Collectors.joining(", "));
            case MethodArgumentNotValidException methodArgumentException ->
                    methodArgumentException.getBindingResult()
                            .getFieldErrors()
                            .stream()
                            .map(error -> error.getField() + ": " + error.getDefaultMessage())
                            .collect(Collectors.joining(", "));
            default -> "Validation error";
        };

        log.error("Validation error: {}", errorMessage);
        return BaseRes.error(errorMessage, 100, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseRes<String>> handleInternalServerError(Exception e) {
        log.error("Internal server error: ", e);
        return BaseRes.error("An unexpected error occurred: " + e.getMessage(), 101, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<BaseRes<String>> handleRateLimitException(RequestNotPermitted e) {
        log.error("Resilience 4j error: ", e);
        return BaseRes.error("Resilience 4j error: " + e.getMessage(), 104, HttpStatus.TOO_MANY_REQUESTS);
    }

    // Custom Exceptions
    @ExceptionHandler({
            AuthException.class,
            UserException.class,
            JobSeekerException.class
    })
    public ResponseEntity<BaseRes<String>> handleCustomException(CustomException e) {
        log.error("Authentication error: {}", e.getMessage());
        return BaseRes.error(e.getMessage(), e.getErrorCode().getCode(), e.getErrorCode().getHttpStatus());
    }
}
