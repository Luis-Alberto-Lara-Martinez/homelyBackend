package org.educa.homelyBackend.controller;

import org.educa.homelyBackend.entity.UserRoles;
import org.educa.homelyBackend.entity.UserStatuses;
import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.service.common.JwtService;
import org.educa.homelyBackend.service.domain.UserRolesService;
import org.educa.homelyBackend.service.domain.UserStatusesService;
import org.educa.homelyBackend.service.domain.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class LoginController extends BaseController {

    private final UsersService usersService;
    private final UserRolesService userRolesService;
    private final UserStatusesService userStatusesService;

    public LoginController(UsersService usersService, UserRolesService userRolesService, UserStatusesService userStatusesService, JwtService jwtService) {
        this.usersService = usersService;
        this.userRolesService = userRolesService;
        this.userStatusesService = userStatusesService;
    }

    @PostMapping("/oauth2/login")
    public ResponseEntity<?> login(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaim("email");
        String name = jwt.getClaim("name");

        if (email == null)
            return badRequestCustomized("El token no contiene el claim 'email'");

        if (name == null)
            return badRequestCustomized("El token no contiene el claim 'name'");

        Optional<Users> user = usersService.findByEmail(email);

        if (user.isEmpty()) {
            Optional<UserRoles> userRol = userRolesService.findByName("USER");
            Optional<UserStatuses> userStatus = userStatusesService.findByName("ACTIVE");

            if (userRol.isEmpty()) return badRequestCustomized("No se encontró el rol 'USER' en la base de datos");
            if (userStatus.isEmpty())
                return badRequestCustomized("No se encontró el estado 'ACTIVE' en la base de datos");

            Users newUser = new Users();
            newUser.setIdRole(userRol.get());
            newUser.setIdStatus(userStatus.get());
            newUser.setImageUrl("https://res.cloudinary.com/homely-cloudinary/image/upload/v1767874172/perfil.png");
            newUser.setName(name);
            newUser.setEmail(email);

            usersService.save(newUser);
        }

        // TODO: terminar función

        Map<String, String> message = Map.of(
                "message", "Inicio de sesión correcto",
                "token", ""
        );
        return ResponseEntity.ok(message);
    }
}