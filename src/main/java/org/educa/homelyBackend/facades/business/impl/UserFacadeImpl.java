package org.educa.homelyBackend.facades.business.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.UserDto;
import org.educa.homelyBackend.dtos.UserProfileDto;
import org.educa.homelyBackend.dtos.requests.CreateUserDtoRequest;
import org.educa.homelyBackend.dtos.requests.PasswordDtoRequest;
import org.educa.homelyBackend.facades.business.UserFacade;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.UserRoleService;
import org.educa.homelyBackend.services.business.UserService;
import org.educa.homelyBackend.services.business.UserStatusService;
import org.educa.homelyBackend.services.shared.AvatarService;
import org.educa.homelyBackend.services.shared.CloudinaryService;
import org.educa.homelyBackend.services.shared.PasswordEncoderService;
import org.educa.homelyBackend.services.shared.ResendService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoderService passwordEncoderService;
    private final UserRoleService userRoleService;
    private final UserStatusService userStatusService;
    private final AvatarService avatarService;
    private final ResendService resendService;

    @Override
    public Page<UserDto> findAllUsers(Integer page, Integer size) {
        Page<UserModel> userPage = userService.findAll(page, size);

        return userPage.map(user -> {
            String createdBy = (user.getCreatedBy() != null) ? user.getCreatedBy().getName() : null;
            String updatedBy = (user.getUpdatedBy() != null) ? user.getUpdatedBy().getName() : null;

            return UserDto.builder()
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
    public UserDto findUser(String email) {
        UserModel user = userService.findByEmailOrThrow(email.toLowerCase());
        String createdBy = (user.getCreatedBy() != null) ? user.getCreatedBy().getName() : null;
        String updatedBy = (user.getUpdatedBy() != null) ? user.getUpdatedBy().getName() : null;

        return UserDto.builder()
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
    public UserProfileDto findUserProfile(String email) {
        UserModel user = userService.findByEmailOrThrow(email.toLowerCase());
        return UserProfileDto.builder()
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createUserByAdmin(String email, CreateUserDtoRequest request) {
        if (request.password() == null || !request.password().equals(request.confirmedPassword())) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "Las contraseñas no coinciden"
            ).get();
        }

        UserModel createdUser = userService.save(
                UserModel.builder()
                        .role(userRoleService.findByNameOrThrow(request.role()))
                        .status(userStatusService.findByNameOrThrow(request.status()))
                        .name(request.name())
                        .email(request.email())
                        .hashedPassword(passwordEncoderService.generateHashedPassword(request.password()))
                        .createdBy(userService.findByEmailOrThrow(email.toLowerCase()))
                        .updatedBy(userService.findByEmailOrThrow(email.toLowerCase()))
                        .build()
        );

        createdUser.setImageUrl(cloudinaryService.uploadAvatarImage(avatarService.generateAvatar(request.name()), createdUser.getId()));
        userService.save(createdUser);
        resendService.sendWelcomeEmail(request.email(), request.name());
    }

    @Override
    public void deleteUser(String email) {
        userService.delete(userService.findByEmailOrThrow(email.toLowerCase()));
    }

    @Override
    public UserProfileDto updateUserProfile(String email, MultipartFile avatarFile, String name) {
        String newImageUrl = null;
        UserModel user = userService.findByEmailOrThrow(email.toLowerCase());

        if (avatarFile != null) {
            newImageUrl = cloudinaryService.uploadAvatarImage(avatarFile, user.getId());
        }

        UserModel modifiedUser = userService.update(
                email,
                UserModel.builder()
                        .name(name)
                        .imageUrl(newImageUrl)
                        .build()
        );

        return UserProfileDto.builder()
                .name(modifiedUser.getName())
                .imageUrl(modifiedUser.getImageUrl())
                .build();
    }

    @Override
    public void updateUserPassword(String email, PasswordDtoRequest request) {
        if (request.password() == null || !request.password().equals(request.confirmedPassword())) {
            throw ExceptionUtil.manageException(
                    HttpStatus.BAD_REQUEST,
                    "Las contraseñas no coinciden"
            ).get();
        }

        userService.update(
                email,
                UserModel.builder()
                        .hashedPassword(passwordEncoderService.generateHashedPassword(request.password()))
                        .build()
        );
    }

    @Override
    public void updateUserRole(String tokenEmail, String email, String role) {
        userService.update(
                email,
                UserModel.builder()
                        .role(userRoleService.findByNameOrThrow(role))
                        .updatedBy(userService.findByEmailOrThrow(tokenEmail))
                        .build()
        );
    }

    @Override
    public void updateUserStatus(String tokenEmail, String email, String status) {
        userService.update(
                email,
                UserModel.builder()
                        .status(userStatusService.findByNameOrThrow(status))
                        .updatedBy(userService.findByEmailOrThrow(tokenEmail))
                        .build()
        );
    }
}
