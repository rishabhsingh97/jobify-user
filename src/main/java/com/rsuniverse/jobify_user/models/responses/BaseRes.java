package com.rsuniverse.jobify_user.models.responses;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class BaseRes<T> {

    private T data;
    private boolean success;
    private String message;
    private int errorCode;
    private String requestId;

    public static <T> ResponseEntity<BaseRes<T>> success(T data) {
        BaseRes<T> response = BaseRes.<T>builder()
            .data(data)
            .success(true)
            .message("Request was successful")
            .errorCode(0) // Indicating no error
            .requestId("")
            .build();
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<BaseRes<T>> error(String message, int errorCode, HttpStatus httpStatus) {
        BaseRes<T> response = BaseRes.<T>builder()
            .data(null)
            .success(false)
            .message(message)
            .errorCode(errorCode)
            .requestId("")
            .build();
        return ResponseEntity.status(httpStatus).body(response);
    }
}
