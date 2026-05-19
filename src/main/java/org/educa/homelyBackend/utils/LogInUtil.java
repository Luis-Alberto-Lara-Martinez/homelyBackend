package org.educa.homelyBackend.utils;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.UserRoleService;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.services.business.UserStatusService;
import org.educa.homelyBackend.services.shared.AvatarService;
import org.educa.homelyBackend.services.shared.CloudinaryService;
import org.educa.homelyBackend.services.shared.JwtService;
import org.educa.homelyBackend.services.shared.PasswordEncoderService;
import org.educa.homelyBackend.services.shared.ResendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LogInUtil {

    private final UserService userService;
    private final ResendService resendService;
    private final JwtService jwtService;
    private final PasswordEncoderService passwordEncoderService;
    private final CloudinaryService cloudinaryService;
    private final UserRoleService userRoleService;
    private final UserStatusService userStatusService;
    private final AvatarService avatarService;

    @Transactional(rollbackFor = Exception.class)
    public UserModel createUserAndSendWelcomeEmail(String name, String email, String password) {
        if (userService.findByEmail(email).isPresent()) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "El correo electrónico ya está registrado"
            ).get();
        }

        String hashedPassword = null;
        if (password != null && !password.isBlank()) {
            hashedPassword = passwordEncoderService.generateHashedPassword(password);
        }

        UserModel savedUser = userService.save(UserModel.builder()
                .role(userRoleService.findByNameOrThrow("usuario"))
                .status(userStatusService.findByNameOrThrow("activo"))
                .email(email)
                .name(name)
                .hashedPassword(hashedPassword)
                .build());

        savedUser.setImageUrl(cloudinaryService.uploadAvatarImage(avatarService.generateAvatar(name), savedUser.getId()));
        resendService.sendWelcomeEmail(email, name);
        return userService.save(savedUser);
    }

    public ResponseEntity<Map<String, String>> createResponse(UserModel user) {
        return ResponseEntity.ok(Map.of(
                "message", "Inicio de sesión exitoso",
                "token", jwtService.generatePersonalizedJwt(user.getEmail(), user.getRole().getName())
        ));
    }
}
