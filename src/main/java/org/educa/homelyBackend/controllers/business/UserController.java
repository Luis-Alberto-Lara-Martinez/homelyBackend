package org.educa.homelyBackend.controllers.business;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.UserDto;
import org.educa.homelyBackend.dtos.UserProfileDto;
import org.educa.homelyBackend.dtos.requests.CreateUserDtoRequest;
import org.educa.homelyBackend.dtos.requests.DeleteUserDtoRequest;
import org.educa.homelyBackend.dtos.requests.PageDtoRequest;
import org.educa.homelyBackend.dtos.requests.PasswordDtoRequest;
import org.educa.homelyBackend.dtos.requests.UpdateUserRoleDtoRequest;
import org.educa.homelyBackend.dtos.requests.UpdateUserStatusDtoRequest;
import org.educa.homelyBackend.facades.business.UserFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @GetMapping(ConfigurationRoutes.API + "/user/profile")
    public UserProfileDto findUserProfile(@AuthenticationPrincipal String email) {
        return userFacade.findUserProfile(email);
    }

    @PutMapping(ConfigurationRoutes.API + "/user/profile")
    public UserProfileDto updateUserProfile(
            @AuthenticationPrincipal String email,
            @RequestPart(value = "avatarFile", required = false) MultipartFile avatarFile,
            @RequestPart(value = "name", required = false) String name
    ) {
        return userFacade.updateUserProfile(email, avatarFile, name);
    }

    @PutMapping(ConfigurationRoutes.API + "/user/password")
    public ResponseEntity<Map<String, String>> updateUserPassword(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody PasswordDtoRequest request
    ) {
        userFacade.updateUserPassword(email, request);
        return ResponseEntityUtil.ok("Contraseña actualizada correctamente");
    }

    @PostMapping(ConfigurationRoutes.ADMIN + "/users")
    public Page<UserDto> findAllUsers(@Valid @RequestBody PageDtoRequest request) {
        return userFacade.findAllUsers(request.page(), request.size());
    }

    @GetMapping(ConfigurationRoutes.ADMIN + "/user")
    public UserDto findUser(@RequestParam("email") String email) {
        return userFacade.findUser(email);
    }

    @PostMapping(ConfigurationRoutes.ADMIN + "/user")
    public ResponseEntity<Map<String, String>> createNewUser(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody CreateUserDtoRequest request
    ) {
        userFacade.createUserByAdmin(email, request);
        return ResponseEntityUtil.ok("Usuario creado exitosamente");
    }

    @DeleteMapping(ConfigurationRoutes.ADMIN + "/user")
    public ResponseEntity<Map<String, String>> deleteUser(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody DeleteUserDtoRequest request) {
        userFacade.deleteUser(request.email());
        return ResponseEntityUtil.ok("Usuario eliminado exitosamente");
    }

    @PutMapping(ConfigurationRoutes.ADMIN + "/user/role")
    public ResponseEntity<Map<String, String>> updateUserRole(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody UpdateUserRoleDtoRequest request
    ) {
        userFacade.updateUserRole(email, request.email(), request.role());
        return ResponseEntityUtil.ok("Rol de usuario actualizado exitosamente");
    }

    @PutMapping(ConfigurationRoutes.ADMIN + "/user/status")
    public ResponseEntity<Map<String, String>> updateUserStatus(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody UpdateUserStatusDtoRequest request
    ) {
        userFacade.updateUserStatus(email, request.email(), request.status());
        return ResponseEntityUtil.ok("Estado de usuario actualizado exitosamente");
    }
}