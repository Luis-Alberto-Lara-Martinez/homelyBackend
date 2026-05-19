package org.educa.homelyBackend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserDto(
        Integer id,
        String role,
        String status,
        String imageUrl,
        String name,
        String email,
        String hashedPassword,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Europe/Madrid")
        Instant createdAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Europe/Madrid")
        Instant updatedAt,

        String createdBy,
        String updatedBy
) {
}
