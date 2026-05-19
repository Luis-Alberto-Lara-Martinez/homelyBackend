package org.educa.homelyBackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record WorkWithUsEmailDtoRequest(
        @NotBlank(message = "El campo 'from' es requerido")
        String from,

        @NotBlank(message = "El campo 'name' es requerido")
        String name,

        @NotBlank(message = "El campo 'workingArea' es requerido")
        String workingArea,

        @NotBlank(message = "El campo 'phone' es requerido")
        String phone,

        @NotBlank(message = "El campo 'description' es requerido")
        String description,

        @NotNull(message = "El campo 'cvFile' es requerido")
        MultipartFile cvFile
) {
}
