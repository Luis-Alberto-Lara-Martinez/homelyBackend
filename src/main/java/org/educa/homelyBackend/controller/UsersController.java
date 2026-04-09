package org.educa.homelyBackend.controller;

import com.resend.core.exception.ResendException;
import jakarta.validation.Valid;
import org.educa.homelyBackend.dto.CheckResetTokenRequest;
import org.educa.homelyBackend.dto.ForgetUserPasswordRequest;
import org.educa.homelyBackend.entity.ResetPasswordTokens;
import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.service.common.CloudinaryService;
import org.educa.homelyBackend.service.domain.ResetPasswordTokensService;
import org.educa.homelyBackend.service.domain.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UsersController extends BaseController {

    private final UsersService usersService;
    private final CloudinaryService cloudinaryService;
    private final ResetPasswordTokensService resetPasswordTokensService;

    public UsersController(UsersService usersService, CloudinaryService cloudinaryService, ResetPasswordTokensService resetPasswordTokensService) {
        this.usersService = usersService;
        this.cloudinaryService = cloudinaryService;
        this.resetPasswordTokensService = resetPasswordTokensService;
    }

    @GetMapping("/api/datos-personales/obtener")
    public ResponseEntity<?> getPersonalUserData(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();

        Optional<Users> userLooked = usersService.findByEmail(email);

        if (userLooked.isEmpty())
            return badRequestCustomized("No se ha encontrado ningún usuario con el email proporcionado");

        Users user = userLooked.get();

        return ResponseEntity.ok(Map.of(
                "name", user.getName(),
                "imageUrl", user.getImageUrl()
        ));
    }

    @PutMapping("/api/datos-personales/actualizar")
    public ResponseEntity<Map<String, String>> updatePersonalUserData(
            @AuthenticationPrincipal Jwt jwt,
            @RequestPart(value = "rawImage", required = false) MultipartFile rawImage,
            @RequestPart(value = "name", required = false) String name,
            @RequestPart(value = "password", required = false) String password,
            @RequestPart(value = "confirmedPassword", required = false) String confirmedPassword
    ) {
        boolean makeChanges = false;
        String email = jwt.getSubject();

        Optional<Users> userLooked = usersService.findByEmail(email);

        if (userLooked.isEmpty())
            return badRequestCustomized("No se ha encontrado ningún usuario con el email proporcionado");

        Users user = userLooked.get();

        if (name != null && !name.trim().equals(user.getName())) {
            user.setName(name.trim());
            makeChanges = true;
        }

        if (password != null) {
            if (password.equals(confirmedPassword)) {
                user.setHashPassword(usersService.encodePassword(password));
                makeChanges = true;
            } else {
                return badRequestCustomized("Las contraseñas no coinciden");
            }
        }

        if (rawImage != null && !rawImage.isEmpty()) {
            String newImageUrl;
            try {
                newImageUrl = cloudinaryService.uploadFile(rawImage);
            } catch (IOException e) {
                return badRequestCustomized("Error al subir la imagen a Cloudinary");
            }
            user.setImageUrl(newImageUrl);
            makeChanges = true;
        }

        if (makeChanges) {
            usersService.saveUser(user);
            return okRequestCustomized("Datos personales actualizados correctamente");
        } else {
            return okRequestCustomized("No se encontró ningún dato nuevo para actualizar");
        }
    }

    @PostMapping("/olvidar-contrasena")
    public ResponseEntity<Map<String, String>> forgetUserPassword(@Valid @RequestBody ForgetUserPasswordRequest request) {
        String email = request.email().toLowerCase();

        Optional<Users> userLooked = usersService.findByEmail(email);

        if (userLooked.isEmpty())
            return badRequestCustomized("No se ha encontrado ningún usuario con el email proporcionado");

        try {
            resetPasswordTokensService.processNewResetPasswordTokens(userLooked.get());
        } catch (ResendException e) {
            return badRequestCustomized("Error al enviar el email de recuperación");
        }

        return okRequestCustomized("Email de recuperación enviado exitosamente");
    }

    @GetMapping("/comprobar-reset-token")
    public ResponseEntity<Map<String, String>> checkResetToken(@Valid @RequestBody CheckResetTokenRequest request) {
        String email = request.email().toLowerCase();
        String token = request.token();

        Optional<Users> userLooked = usersService.findByEmail(email);

        if (userLooked.isEmpty())
            return badRequestCustomized("No se ha encontrado ningún usuario con el email proporcionado");

        List<ResetPasswordTokens> resetPasswordTokensList = resetPasswordTokensService.findByUser(userLooked.get());

        if (resetPasswordTokensList.isEmpty())
            return badRequestCustomized("No se han encontrado tokens de recuperación para el usuario proporcionado");

        for (ResetPasswordTokens resetPasswordToken : resetPasswordTokensList) {
            if (resetPasswordToken.getExpiration().isAfter(Instant.now()) && resetPasswordTokensService.checkToken(token, resetPasswordToken.getHashToken())) {
                return okRequestCustomized("Token válido");
            }
        }

        return badRequestCustomized("No se encontró ningún token válido para el usuario proporcionado");
    }
}