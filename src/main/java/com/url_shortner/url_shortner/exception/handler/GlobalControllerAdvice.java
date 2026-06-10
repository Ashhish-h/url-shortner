package com.url_shortner.url_shortner.exception.handler;

import com.url_shortner.url_shortner.dto.ErrorResponse;
import com.url_shortner.url_shortner.exception.ErrorCodes;
import com.url_shortner.url_shortner.exception.UrlNotAccessibleException;
import com.url_shortner.url_shortner.exception.UrlNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUrlNotFound(UrlNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCodes.NOT_FOUND.getCode())
                .message(ErrorCodes.NOT_FOUND.getMessage())
                .error(ex.getMessage())
                .path(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(UrlNotAccessibleException.class)
    public ResponseEntity<ErrorResponse> handleUrlNotAccessible(UrlNotAccessibleException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCodes.BAD_REQUEST.getCode())
                .message(ErrorCodes.BAD_REQUEST.getMessage())
                .error(ex.getMessage())
                .path(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCodes.INTERNAL_SERVER_ERROR.getCode())
                .message(ErrorCodes.INTERNAL_SERVER_ERROR.getMessage())
                .error(ex.getMessage())
                .path(request.getRequestURL().toString())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

}
