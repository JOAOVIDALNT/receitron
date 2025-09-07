package com.joaovidal.receitron.adapter.in.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        return switch (ex) {
            case BadCredentialsException e -> createProblemDetail(401, e.getMessage());
            case AccountStatusException e -> createProblemDetail(403, e.getMessage(), "The account is locked");
            case AccessDeniedException e -> createProblemDetail(403, e.getMessage(), "You are not authorized to access this resource");
            case SignatureException e -> createProblemDetail(403, e.getMessage(), "The JWT signature is invalid");
            case ExpiredJwtException e -> createProblemDetail(403, e.getMessage(), "The JWT token has expired");
            default -> createProblemDetail(500, ex.getMessage(), "Unknown internal server error.");
        };
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ex) {
        if (ex instanceof UsernameNotFoundException) {
            return createProblemDetail(404, ex.getMessage());
        }
        return createProblemDetail(500, ex.getMessage(), "Unknown internal server error.");
    }

    private ProblemDetail createProblemDetail(int status, String detail) {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(status), detail);
    }

    private ProblemDetail createProblemDetail(int status, String detail, String description) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(status), detail);
        problem.setProperty("description", description);
        return problem;
    }

}
