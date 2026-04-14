package org.educa.homelyBackend.controller;

import com.resend.core.exception.ResendException;
import jakarta.validation.Valid;
import org.educa.homelyBackend.dto.*;
import org.educa.homelyBackend.entity.ResetPasswordTokens;
import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.service.common.JwtService;
import org.educa.homelyBackend.service.domain.ResetPasswordTokensService;
import org.educa.homelyBackend.service.domain.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ResetPasswordTokensController extends BaseController {

    // TODO: revisar archivo entero

    private final UsersService usersService;
    private final ResetPasswordTokensService resetPasswordTokensService;

    public ResetPasswordTokensController(UsersService usersService, ResetPasswordTokensService resetPasswordTokensService) {
        this.usersService = usersService;
        this.resetPasswordTokensService = resetPasswordTokensService;
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

        List<ResetPasswordTokens> resetPasswordTokensList = resetPasswordTokensService.findByUserOrderByCreatedAtDesc(userLooked.get());

        if (resetPasswordTokensList.isEmpty())
            return badRequestCustomized("No se han encontrado tokens de recuperación para el usuario proporcionado");

        for (ResetPasswordTokens resetPasswordToken : resetPasswordTokensList) {
            if (!resetPasswordToken.getUsed() && resetPasswordToken.getExpiration().isAfter(Instant.now()) && resetPasswordTokensService.checkToken(token, resetPasswordToken.getHashToken())) {
                return okRequestCustomized("Token válido");
            }
        }

        return badRequestCustomized("No se encontró ningún token válido para el usuario proporcionado");
    }

    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<Map<String, String>> resetUserPassword(@Valid @RequestBody ResetUserPasswordRequest request) {
        String token = request.token();
        String email = request.email().toLowerCase();
        String password = request.password();
        String confirmedPassword = request.confirmedPassword();

        if (!password.equals(confirmedPassword)) return badRequestCustomized("Las contraseñas no coinciden");

        Optional<Users> userLooked = usersService.findByEmail(email);

        if (userLooked.isEmpty())
            return badRequestCustomized("No se ha encontrado ningún usuario con el email proporcionado");

        if (resetPasswordTokensService.processNewResetPasswordTokensJustUsed(userLooked.get(), password, token)) {
            return okRequestCustomized("Contraseña restablecida exitosamente");
        } else {
            return badRequestCustomized("No se encontró ningún token válido para el usuario proporcionado");
        }
    }
}