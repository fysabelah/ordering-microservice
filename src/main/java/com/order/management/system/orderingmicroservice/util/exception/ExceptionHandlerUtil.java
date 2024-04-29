package com.order.management.system.orderingmicroservice.util.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandlerUtil {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<StandardError> genericError(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status)
                .body(new StandardError(status.value(),
                        "Unknown Error",
                        e.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<StandardError> dtoValidations(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        StandardError error = new StandardError(status.value(),
                "Data Error",
                e.getFieldError() == null ?
                        e.getMessage() :
                        e.getFieldError().getField() + ": " + e.getFieldError().getDefaultMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardError> missingParameters(IllegalArgumentException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError error = new StandardError(status.value(),
                "Parameters error",
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardError> notFound(NoSuchElementException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError error = new StandardError(status.value(),
                "Parameters error",
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }
}
