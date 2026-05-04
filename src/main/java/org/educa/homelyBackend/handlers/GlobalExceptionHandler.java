package org.educa.homelyBackend.handlers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Error inesperado en la validación de los argumentos");

        return ResponseEntityUtil.personalizedError(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationExceptions(ConstraintViolationException exception) {
        String errorMessage = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Error inesperado en la validación de los argumentos");

        return ResponseEntityUtil.personalizedError(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusExceptions(ResponseStatusException exception) {
        String error = Optional.ofNullable(exception.getReason())
                .orElse("Error inesperado producido por ResponseStatusException");

        return ResponseEntityUtil.personalizedError(exception.getStatusCode(), error);
    }
}
