package org.educa.homelyBackend.utils;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseEntityUtil {

    public static ResponseEntity<Map<String, String>> ok(String message) {
        return ResponseEntity.ok(Map.of("message", message));
    }

    public static ResponseEntity<Map<String, String>> badRequest(String error) {
        return ResponseEntity.badRequest().body(Map.of("error", error));
    }
}
