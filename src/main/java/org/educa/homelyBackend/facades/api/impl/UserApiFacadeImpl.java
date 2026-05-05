package org.educa.homelyBackend.facades.api.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.UpdateUserPasswordRequest;
import org.educa.homelyBackend.dtos.responses.UserProfileResponse;
import org.educa.homelyBackend.facades.api.UserApiFacade;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserApiFacadeImpl implements UserApiFacade {

    private final UserService userService;

    @Override
    public UserProfileResponse findUserProfile(Jwt jwt) {
        UserModel user = userService.findByEmailOrThrow(jwt.getSubject());

        return UserProfileResponse.builder()
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .build();
    }

    @Override
    public UserProfileResponse updateUserProfile(Jwt jwt, MultipartFile avatarFile, String name) {
        UserModel user = userService.findByEmailOrThrow(jwt.getSubject());

        if (avatarFile != null) {
            user = userService.updateImage(user, avatarFile);
        }

        if (name != null) {
            user = userService.updateName(user, name);
        }

        return UserProfileResponse.builder()
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .build();
    }

    @Override
    public ResponseEntity<Map<String, String>> updateUserPassword(Jwt jwt, UpdateUserPasswordRequest request) {
        String password = request.password();

        if (!password.equals(request.confirmedPassword())) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "Las contraseñas no coinciden"
            ).get();
        }

        userService.updateHashedPassword(userService.findByEmailOrThrow(jwt.getSubject()), password);

        return ResponseEntityUtil.ok("Contraseña actualizada correctamente");
    }
}
