package org.educa.homelyBackend.facades.admin.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.requests.CreateNewUserRequest;
import org.educa.homelyBackend.dtos.responses.FindAllUsersResponse;
import org.educa.homelyBackend.facades.admin.UserAdminFacade;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdminFacadeImpl implements UserAdminFacade {

    private final UserService userService;

    @Override
    public FindAllUsersResponse findUser(String email) {
        UserModel user = userService.findByEmailOrThrow(email);

        String createdBy = null;
        String updatedBy = null;

        if (user.getCreatedBy() != null) {
            createdBy = user.getCreatedBy().getName();
        }
        if (user.getUpdatedBy() != null) {
            updatedBy = user.getUpdatedBy().getName();
        }
        return FindAllUsersResponse.builder()
                .id(user.getId())
                .role(user.getRole().getName())
                .status(user.getStatus().getName())
                .imageUrl(user.getImageUrl())
                .name(user.getName())
                .email(user.getEmail())
                .hashedPassword(user.getHashedPassword())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .build();
    }

    @Override
    public Page<FindAllUsersResponse> findAll(Integer page, Integer size, String sortBy) {
        return userService.findAll(page, size, sortBy).map(user -> {
            String createdBy = null;
            String updatedBy = null;
            if (user.getCreatedBy() != null) {
                createdBy = user.getCreatedBy().getName();
            }
            if (user.getUpdatedBy() != null) {
                updatedBy = user.getUpdatedBy().getName();
            }

            return FindAllUsersResponse.builder()
                    .id(user.getId())
                    .role(user.getRole().getName())
                    .status(user.getStatus().getName())
                    .imageUrl(user.getImageUrl())
                    .name(user.getName())
                    .email(user.getEmail())
                    .hashedPassword(user.getHashedPassword())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .createdBy(createdBy)
                    .updatedBy(updatedBy)
                    .build();
        });
    }

    @Override
    public void createNewUser(String tokenEmail, CreateNewUserRequest request) {
        if (!request.password().equals(request.confirmedPassword())) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden").get();
        }

        UserModel user = userService.createUser(
                request.email(),
                request.name(),
                request.password(),
                request.role(),
                request.status()
        );

        userService.updateCreatedBy(user, userService.findByEmailOrThrow(tokenEmail));
        userService.updateUpdatedBy(user, userService.findByEmailOrThrow(tokenEmail));
    }

    @Override
    public void deleteUser(Integer id) {
        userService.delete(id);
    }

    @Override
    public void updateRole(String email, String role, String tokenEmail) {
        UserModel user = userService.findByEmailOrThrow(email);
        userService.updateRole(user, role);
        userService.updateUpdatedBy(user, userService.findByEmailOrThrow(tokenEmail));
    }

    @Override
    public void updateStatus(String email, String status, String tokenEmail) {
        UserModel user = userService.findByEmailOrThrow(email);
        userService.updateStatus(user, status);
        userService.updateUpdatedBy(user, userService.findByEmailOrThrow(tokenEmail));
    }
}


