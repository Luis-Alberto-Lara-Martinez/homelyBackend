package org.educa.homelyBackend.dtos.requests;

import org.springframework.web.multipart.MultipartFile;

public record CreatePropertyImageDtoRequest(
        MultipartFile image,
        Integer displayOrder
) {
}
