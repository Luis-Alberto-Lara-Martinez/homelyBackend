package org.educa.homelyBackend.controllers.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.CreateNewUserRequest;
import org.educa.homelyBackend.dtos.requests.DeleteUserRequest;
import org.educa.homelyBackend.dtos.requests.FindAllUsersRequest;
import org.educa.homelyBackend.dtos.requests.UpdateUserRoleRequest;
import org.educa.homelyBackend.dtos.requests.UpdateUserStatusRequest;
import org.educa.homelyBackend.dtos.responses.FindAllUsersResponse;
import org.educa.homelyBackend.facades.admin.UserAdminFacade;
import org.educa.homelyBackend.routes.ConfigurationRoutes;
import org.educa.homelyBackend.utils.ResponseEntityUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(ConfigurationRoutes.ADMIN)
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminFacade userAdminFacade;

    @PostMapping("/users")
    public Page<FindAllUsersResponse> findAllUsers(@Valid @RequestBody FindAllUsersRequest request) {
        return userAdminFacade.findAll(request.page(), request.size(), request.sortBy());
    }

    @PostMapping("/user")
    public ResponseEntity<Map<String, String>> createNewUser(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody CreateNewUserRequest request
    ) {
        userAdminFacade.createNewUser(email, request);
        return ResponseEntityUtil.ok("Usuario creado exitosamente");
    }

    @DeleteMapping("/user")
    public ResponseEntity<Map<String, String>> deleteUser(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody DeleteUserRequest request) {
        userAdminFacade.deleteUser(request.id());
        return ResponseEntityUtil.ok("Usuario eliminado exitosamente");
    }

    @PutMapping("/user/role")
    public ResponseEntity<Map<String, String>> updateUserRole(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody UpdateUserRoleRequest request
    ) {
        userAdminFacade.updateRole(request.email(), request.role(), email);
        return ResponseEntityUtil.ok("Rol de usuario actualizado exitosamente");
    }

    @PutMapping("/user/status")
    public ResponseEntity<Map<String, String>> updateUserStatus(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {
        userAdminFacade.updateStatus(request.email(), request.status(), email);
        return ResponseEntityUtil.ok("Estado de usuario actualizado exitosamente");
    }
}
