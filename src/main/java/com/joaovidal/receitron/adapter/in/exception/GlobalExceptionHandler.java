package com.joaovidal.receitron.adapter.in.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex) {
        return buildExceptionResponse(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        return switch (ex) {
            case BadCredentialsException e -> buildExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
            case AccountStatusException e -> buildExceptionResponse(e.getMessage(), HttpStatus.FORBIDDEN);
            case AccessDeniedException e -> buildExceptionResponse(e.getMessage(), HttpStatus.FORBIDDEN);
            case SignatureException e -> buildExceptionResponse(e.getMessage(), HttpStatus.FORBIDDEN);
            case ExpiredJwtException e -> buildExceptionResponse(e.getMessage(), HttpStatus.FORBIDDEN);
            default -> buildUnknowError();
        };
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        if (ex instanceof UsernameNotFoundException) {
            return buildExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return buildUnknowError();
    }

    private ResponseEntity<Object> buildExceptionResponse(String message, HttpStatus status) {
        var body = Map.of(
                "error", message,
                "status", status.value()
        );
        return new ResponseEntity<>(body, status);
    }

    private ResponseEntity<Object> buildUnknowError() {
        return new ResponseEntity<>("Unknow internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
