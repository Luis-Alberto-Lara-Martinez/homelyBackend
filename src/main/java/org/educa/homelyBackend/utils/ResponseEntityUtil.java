package org.educa.homelyBackend.utils;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseEntityUtil {

    public static ResponseEntity<Map<String, String>> ok(String message) {
        return ResponseEntity.ok(Map.of("message", message));
    }

    public static ResponseEntity<Map<String, String>> personalizedError(HttpStatusCode httpStatusCode, String error) {
        return ResponseEntity.status(httpStatusCode).body(Map.of("error", error));
    }
}
