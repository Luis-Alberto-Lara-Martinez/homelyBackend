package org.educa.homelyBackend.facades.api;

import org.educa.homelyBackend.dtos.requests.UpdateUserPasswordRequest;
import org.educa.homelyBackend.dtos.responses.UserProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserApiFacade {

    UserProfileResponse findUserProfile(String email);

    UserProfileResponse updateUserProfile(String email, MultipartFile avatarFile, String name);

    ResponseEntity<Map<String, String>> updateUserPassword(String email, UpdateUserPasswordRequest request);
}
