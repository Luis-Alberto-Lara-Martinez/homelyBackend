package org.educa.homelyBackend.controllers;

import com.resend.core.exception.ResendException;
import jakarta.validation.Valid;
import org.educa.homelyBackend.dtos.LoginTraditionalRequest;
import org.educa.homelyBackend.dtos.RegisterTraditionalRequest;
import org.educa.homelyBackend.services.common.TokenService;
import org.educa.homelyBackend.services.dedicated.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
public class LoginController extends BaseController {

    private final UsersService usersService;
    private final TokenService jwtService;

    public LoginController(UsersService usersService, TokenService jwtService) {
        this.usersService = usersService;
        this.jwtService = jwtService;
    }

    @PostMapping("/oauth2/login")
    public ResponseEntity<Map<String, String>> oauth2LoginAndRegister(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");

        if (email == null) return badRequestCustomized("El token no contiene el claim 'email'");
        if (name == null) return badRequestCustomized("El token no contiene el claim 'name'");

        Optional<Users> searchedUser = usersService.findByEmail(email);
        Users user;

        if (searchedUser.isEmpty()) {
            Optional<Users> nuevoUsuario;
            try {
                nuevoUsuario = usersService.getUserOrCreateNewUser(name, email, null);
            } catch (ResendException e) {
                return badRequestCustomized("No se pudo crear el usuario porque ocurrió un error al enviar el email de bienvenida");
            } catch (IOException e) {
                return badRequestCustomized("No se pudo crear el usuario porque ocurrió un error al generar el avatar");
            }

            if (nuevoUsuario.isEmpty())
                return badRequestCustomized("No se pudo crear el usuario porque no se encontró el rol o el status especificados");

            user = nuevoUsuario.get();
        } else {
            user = searchedUser.get();
        }

        return createLoginResponse(user);
    }

    @PostMapping("/local/login")
    public ResponseEntity<Map<String, String>> loginTraditional(@Valid @RequestBody LoginTraditionalRequest request) {
        String email = request.email().toLowerCase();
        String password = request.password();

        Optional<Users> searchedUser = usersService.findByEmail(email);

        if (searchedUser.isEmpty()) return badRequestCustomized("No existe ningún usuario con ese email");

        Users user = searchedUser.get();

        if (user.getHashPassword() == null)
            return badRequestCustomized("El usuario no tiene contraseña local");

        if (!usersService.checkPassword(password, user.getHashPassword()))
            return badRequestCustomized("La contraseña es incorrecta");

        return createLoginResponse(user);
    }

    @PostMapping("/local/register")
    public ResponseEntity<Map<String, String>> registerTraditional(@Valid @RequestBody RegisterTraditionalRequest request) {
        String name = request.name().trim();
        String email = request.email().toLowerCase();
        String password = request.password();
        String confirmedPassword = request.confirmedPassword();

        if (!password.equals(confirmedPassword))
            return badRequestCustomized("La password y la confirmedPassword no coinciden");

        Optional<Users> searchedUser = usersService.findByEmail(email);

        if (searchedUser.isPresent()) return badRequestCustomized("Ya existe un usuario con ese email");

        Optional<Users> nuevoUsuario;
        try {
            nuevoUsuario = usersService.getUserOrCreateNewUser(name, email, password);
        } catch (ResendException e) {
            return badRequestCustomized("No se pudo crear el usuario porque ocurrió un error al enviar el email de bienvenida");
        } catch (IOException e) {
            return badRequestCustomized("No se pudo crear el usuario porque ocurrió un error al generar el avatar");
        }

        if (nuevoUsuario.isEmpty())
            return badRequestCustomized("No se pudo crear el usuario porque no se encontró el rol o el status especificados");

        Users user = nuevoUsuario.get();

        return createLoginResponse(user);
    }

    private ResponseEntity<Map<String, String>> createLoginResponse(Users user) {
        String token = jwtService.generatePersonalizedJwt(user.getEmail(), Map.of(
                "name", user.getName(),
                "role", user.getIdRole().getName()
        ));

        return ResponseEntity.ok(Map.of(
                "message", "Inicio de sesión correcto",
                "token", token
        ));
    }
}