package org.educa.homelyBackend.controller;

import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.service.domain.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
public class UsersController extends BaseController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/api/personal-data")
    public ResponseEntity<Map<String, String>> personalUserData(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("rawImage") MultipartFile rawImage,
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("confirmedPassword") String confirmedPassword
    ) {
        String email = jwt.getSubject();

        if (email == null || email.isBlank()) return badRequestCustomized("El token no contiene ningún email");

        Optional<Users> userLooked = usersService.findByEmail(email);

        if (userLooked.isEmpty())
            return badRequestCustomized("No se ha encontrado ningún usuario con el email proporcionado");

        Users user = userLooked.get();

        if (!name.trim().equals(user.getName())) user.setName(name);

        // todo: terminar metodo
        if (!name.trim().equals(user.getName())) user.setName(name);


        return okRequestCustomized("Datos personales actualizados correctamente");
    }
}