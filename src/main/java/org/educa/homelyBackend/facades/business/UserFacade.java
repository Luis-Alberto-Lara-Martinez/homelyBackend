package org.educa.homelyBackend.facades.business;

import org.educa.homelyBackend.dtos.UserDto;
import org.educa.homelyBackend.dtos.UserProfileDto;
import org.educa.homelyBackend.dtos.requests.CreateUserDtoRequest;
import org.educa.homelyBackend.dtos.requests.PasswordDtoRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserFacade {
    Page<UserDto> findAllUsers(Integer page, Integer size);

    UserDto findUser(String email);

    UserProfileDto findUserProfile(String email);

    void createUserByAdmin(String email, CreateUserDtoRequest request);

    void deleteUser(String email);

    UserProfileDto updateUserProfile(String email, MultipartFile avatarFile, String name);

    void updateUserPassword(String email, PasswordDtoRequest request);

    void updateUserRole(String tokenEmail, String email, String role);

    void updateUserStatus(String tokenEmail, String email, String status);
}
