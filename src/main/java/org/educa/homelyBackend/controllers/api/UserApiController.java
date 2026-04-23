package org.educa.homelyBackend.controllers.api;

import jakarta.validation.Valid;
import org.educa.homelyBackend.dtos.request.UpdateUserNameRequest;
import org.educa.homelyBackend.dtos.request.UpdateUserPasswordRequest;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.dedicated.UserService;
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

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> getPersonalUserData(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();

        UserModel user = userService.findByEmailOrThrow(email);

        return ResponseEntity.ok(Map.of(
                "name", user.getName(),
                "imageUrl", user.getImageUrl()
        ));
    }

    @PutMapping("/avatar")
    public ResponseEntity<Map<String, String>> updateUserAvatar(
            @AuthenticationPrincipal
            Jwt jwt,

            @RequestPart(value = "avatarFile")
            MultipartFile avatarFile
    ) {
        String email = jwt.getSubject();

        String newImageUrl = userService.updateImage(email, avatarFile);

        return ResponseEntity.ok(Map.of(
                "imageUrl", newImageUrl
        ));
    }

    @PutMapping("/name")
    public ResponseEntity<Map<String, String>> updateName(
            @AuthenticationPrincipal
            Jwt jwt,

            @Valid @RequestBody UpdateUserNameRequest request
    ) {
        String email = jwt.getSubject();
        String name = request.name();

        String newName = userService.updateName(email, name);

        return ResponseEntity.ok(Map.of(
                "name", newName
        ));
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateUserPasswordRequest request
    ) {
        String email = jwt.getSubject();
        String password = request.password();
        String confirmedPassword = request.confirmedPassword();

        if (!password.equals(confirmedPassword)) {
            return ResponseEntity.badRequest().body(Map.of("message", "The password and the confirmed password do not match"));
        }

        // userService.updateHashedPassword(userService.findByEmailOrThrow(email), password);
        return ResponseEntity.ok().body(Map.of("message", "Password updated successfully"));
    }
}