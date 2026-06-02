package com.josan.study_buddy.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudyBuddyException.class)
    public ResponseEntity<ErrorResponse> handleStudyBuddyException(StudyBuddyException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ErrorResponse.of(ex.getStatus().value(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "Validation failed: " + message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            String accepted = Arrays.stream(ife.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(),
                            "Invalid value '" + ife.getValue() + "'. Accepted values: " + accepted));
        }
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "Malformed request body"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred"));
    }
}
