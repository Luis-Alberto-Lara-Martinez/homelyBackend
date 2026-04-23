package org.educa.homelyBackend.configurations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();

        if (errorMessage == null) {
            errorMessage = "Error inesperado en la validación de los argumentos";
        }

        return ResponseEntityUtil.badRequest(errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationExceptions(ConstraintViolationException exception) {
        String errorMessage = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Error inesperado en la validación de los argumentos");

        return ResponseEntityUtil.badRequest(errorMessage);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusExceptions(ResponseStatusException exception) {
        String error = exception.getReason();

        if (error == null) {
            error = "Error inesperado producido por ResponseStatusException";
        }

        return ResponseEntity.status(exception.getStatusCode()).body(Map.of("error", error));
    }
}
