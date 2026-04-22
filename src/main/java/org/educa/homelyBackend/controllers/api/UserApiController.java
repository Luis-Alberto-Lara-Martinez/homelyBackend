package org.educa.homelyBackend.controllers.api;

import org.educa.homelyBackend.dtos.response.PersonalUserDataResponse;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.dedicated.UserService;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getPersonalUserData(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();

        UserModel user = userService.findByEmailOrThrow(email);

        return ResponseEntity.ok(PersonalUserDataResponse.builder().name(user.getName()).imageUrl(user.getImageUrl()).build());
    }

    @PutMapping("/user")
    public ResponseEntity<Map<String, String>> updatePersonalUserData(@AuthenticationPrincipal Jwt jwt, @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile, @RequestPart(value = "name", required = false) String name, @RequestPart(value = "password", required = false) String password, @RequestPart(value = "confirmedPassword", required = false) String confirmedPassword) {
        String email = jwt.getSubject();


        aaaaa if () {

        }

        if (userService.updateProfile(email, avatarFile, name, password, confirmedPassword)) {
            return ResponseEntityUtil.ok("");
        } else {
            return ResponseEntityUtil.ok("Datos personales actualizados correctamente");
        }
    }
}