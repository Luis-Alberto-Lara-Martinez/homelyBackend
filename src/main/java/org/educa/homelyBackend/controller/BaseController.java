package org.educa.homelyBackend.controller;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public abstract class BaseController {

    protected ResponseEntity<Map<String, String>> badRequestCustomized(String errorMessage) {
        return ResponseEntity.badRequest().body(Map.of(
                "error", errorMessage
        ));
    }

    protected ResponseEntity<Map<String, String>> okRequestCustomized(String message) {
        return ResponseEntity.ok(Map.of(
                "message", message
        ));
    }
}