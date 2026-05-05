package org.educa.homelyBackend.controllers.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.UpdateUserPasswordRequest;
import org.educa.homelyBackend.dtos.responses.UserProfileResponse;
import org.educa.homelyBackend.facades.api.UserApiFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserApiFacade userApiFacade;

    @GetMapping("/")
    public UserProfileResponse findUserProfile(@AuthenticationPrincipal Jwt jwt) {
        return userApiFacade.findUserProfile(jwt);
    }

    @PutMapping("/profile")
    public UserProfileResponse updateUserProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestPart(value = "avatarFile") MultipartFile avatarFile,
            @RequestPart(value = "name") String name
    ) {
        return userApiFacade.updateUserProfile(jwt, avatarFile, name);
    }

    @PutMapping("/password")
    public ResponseEntity<?> updateUserPassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateUserPasswordRequest request
    ) {
        return userApiFacade.updateUserPassword(jwt, request);
    }
}