package org.educa.homelyBackend.controller;

import org.educa.homelyBackend.dto.DatosPersonalesObtenerResponse;
import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.service.common.CloudinaryService;
import org.educa.homelyBackend.service.domain.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
public class UsersController extends BaseController {

    private final UsersService usersService;
    private final CloudinaryService cloudinaryService;

    public UsersController(UsersService usersService, CloudinaryService cloudinaryService) {
        this.usersService = usersService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/api/datos-personales/obtener")
    public ResponseEntity<?> getPersonalUserData(@AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getSubject();

        Optional<Users> userLooked = usersService.findByEmail(email);

        if (userLooked.isEmpty())
            return badRequestCustomized("No se ha encontrado ningún usuario con el email proporcionado");

        Users user = userLooked.get();

        DatosPersonalesObtenerResponse response = new DatosPersonalesObtenerResponse(
                user.getName(),
                user.getImageUrl()
        );

        return ResponseEntity.ok(response);
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
}