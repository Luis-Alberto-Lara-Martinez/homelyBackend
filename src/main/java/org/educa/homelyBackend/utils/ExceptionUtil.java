package org.educa.homelyBackend.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Supplier;

@Slf4j
@UtilityClass
public class ExceptionUtil {

    public static ResponseStatusException manageException(Exception e, HttpStatus status, String reason) {
        log.error("{}: {}", reason, e.getMessage());
        return new ResponseStatusException(status, reason);
    }

    public static Supplier<ResponseStatusException> manageException(HttpStatus status, String reason) {
        return () -> {
            log.error("Error: {}", reason);
            return new ResponseStatusException(status, reason);
        };
    }
}
