package org.educa.homelyBackend.controllers;

import org.educa.homelyBackend.services.common.CloudinaryService;
import org.educa.homelyBackend.services.dedicated.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UsersApiController extends BaseController {

    // TODO: revisar archivo entero

    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    public UsersApiController(UserService userService, CloudinaryService cloudinaryService) {
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/api/user/personal-data")
    public ResponseEntity<?> getPersonalUserData(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();

        Optional<Users> userLooked = userService.findByEmail(email);

        if (userLooked.isEmpty())
            return badRequestCustomized("No se ha encontrado ningún usuario con el email proporcionado");

        Users user = userLooked.get();

        return ResponseEntity.ok(Map.of(
                "name", user.getName(),
                "imageUrl", user.getImageUrl()
        ));
    }

    @PutMapping("/api/actualizar")
    public ResponseEntity<Map<String, String>> updatePersonalUserData(
            @AuthenticationPrincipal Jwt jwt,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile,
            @RequestPart(value = "name", required = false) String name,
            @RequestPart(value = "password", required = false) String password,
            @RequestPart(value = "confirmedPassword", required = false) String confirmedPassword
    ) {
        boolean makeChanges = false;
        String email = jwt.getSubject();

        Optional<Users> userLooked = userService.findByEmail(email);

        if (userLooked.isEmpty())
            return badRequestCustomized("No se ha encontrado ningún usuario con el email proporcionado");

        Users user = userLooked.get();

        if (name != null && !name.trim().equals(user.getName())) {
            user.setName(name.trim());
            makeChanges = true;
        }

        if (password != null) {
            if (password.equals(confirmedPassword)) {
                user.setHashPassword(userService.encodePassword(password));
                makeChanges = true;
            } else {
                return badRequestCustomized("Las contraseñas no coinciden");
            }
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String newImageUrl;
            try {
                newImageUrl = cloudinaryService.uploadAvatarImage(avatarFile, user.getId());
            } catch (IOException e) {
                return badRequestCustomized("Error al subir la imagen a Cloudinary");
            }
            user.setImageUrl(newImageUrl);
            makeChanges = true;
        }

        if (makeChanges) {
            userService.saveOrUpdate(user);
            return okRequestCustomized("Datos personales actualizados correctamente");
        } else {
            return okRequestCustomized("No se encontró ningún dato nuevo para actualizar");
        }
    }
}