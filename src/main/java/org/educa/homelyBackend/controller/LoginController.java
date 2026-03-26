package org.educa.homelyBackend.controller;

import org.educa.homelyBackend.dto.LoginTraditionalRequest;
import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.service.common.JwtService;
import org.educa.homelyBackend.service.domain.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class LoginController extends BaseController {

    private final UsersService usersService;
    private final JwtService jwtService;

    public LoginController(UsersService usersService, JwtService jwtService) {
        this.usersService = usersService;
        this.jwtService = jwtService;
    }

    @PostMapping("/oauth2/login")
    public ResponseEntity<Map<String, String>> oauth2LoginAndRegister(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");

        if (email == null)
            return badRequestCustomized("El token no contiene el claim 'email'");

        if (name == null)
            return badRequestCustomized("El token no contiene el claim 'name'");

        Optional<Users> searchedUser = usersService.findByEmail(email);
        Users user;

        if (searchedUser.isEmpty()) {
            Optional<Users> nuevoUsuario = usersService.processOath2(name, email);

            if (nuevoUsuario.isEmpty())
                return badRequestCustomized("No se pudo crear el usuario porque no se encontró el rol o el status especificados");

            user = nuevoUsuario.get();
        } else {
            user = searchedUser.get();
        }

        return createLoginResponse(user);
    }

    @PostMapping("/local/login")
    public ResponseEntity<Map<String, String>> loginTraditional(@RequestBody LoginTraditionalRequest request) {
        String email = request.email();
        String password = request.password();

        if (email == null) return badRequestCustomized("El email es requerido");
        if (password == null) return badRequestCustomized("La password es requerida");

        Optional<Users> searchedUser = usersService.findByEmail(email);

        if (searchedUser.isEmpty()) return badRequestCustomized("No existe ningún usuario con ese email");

        Users user = searchedUser.get();

        if (user.getHashPassword() == null)
            return badRequestCustomized("El usuario no tiene contraseña local");

        if (!usersService.checkPassword(password, user.getHashPassword()))
            return badRequestCustomized("La contraseña es incorrecta");

        return createLoginResponse(user);
    }

    private ResponseEntity<Map<String, String>> createLoginResponse(Users user) {
        String token = jwtService.generateToken(user.getEmail(), Map.of(
                "name", user.getName(),
                "role", user.getIdRole().getName()
        ));

        return ResponseEntity.ok(Map.of(
                "message", "Inicio de sesión correcto",
                "token", token
        ));
    }
}