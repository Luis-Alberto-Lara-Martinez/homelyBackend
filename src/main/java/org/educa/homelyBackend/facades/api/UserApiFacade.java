package org.educa.homelyBackend.facades.api;

import org.educa.homelyBackend.dtos.requests.UpdateUserPasswordRequest;
import org.educa.homelyBackend.dtos.responses.UserProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserApiFacade {
    UserProfileResponse findUserProfile(Jwt jwt);

    UserProfileResponse updateUserProfile(Jwt jwt, MultipartFile avatarFile, String name);

    ResponseEntity<Map<String, String>> updateUserPassword(Jwt jwt, UpdateUserPasswordRequest request);
}
