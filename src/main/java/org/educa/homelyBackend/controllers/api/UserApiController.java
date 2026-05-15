package org.educa.homelyBackend.controllers.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.UpdateUserPasswordRequest;
import org.educa.homelyBackend.dtos.responses.UserProfileResponse;
import org.educa.homelyBackend.facades.api.UserApiFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
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
@RequestMapping(ConfigurationRoutes.API + "/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserApiFacade userApiFacade;

    @GetMapping("/profile")
    public UserProfileResponse findUserProfile(@AuthenticationPrincipal String email) {
        return userApiFacade.findUserProfile(email);
    }

    @PutMapping("/profile")
    public UserProfileResponse updateUserProfile(
            @AuthenticationPrincipal String email,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile,
            @RequestPart(value = "name", required = false) String name
    ) {
        return userApiFacade.updateUserProfile(email, avatarFile, name);
    }

    @PutMapping("/password")
    public ResponseEntity<?> updateUserPassword(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody UpdateUserPasswordRequest request
    ) {
        return userApiFacade.updateUserPassword(email, request);
    }
}