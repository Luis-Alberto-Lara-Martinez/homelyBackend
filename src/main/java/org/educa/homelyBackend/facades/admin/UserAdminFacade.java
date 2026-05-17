package org.educa.homelyBackend.facades.admin;

import org.educa.homelyBackend.dtos.requests.CreateNewUserRequest;
import org.educa.homelyBackend.dtos.responses.FindAllUsersResponse;
import org.springframework.data.domain.Page;

public interface UserAdminFacade {
    FindAllUsersResponse findUser(String email);

    Page<FindAllUsersResponse> findAll(Integer page, Integer size, String sortBy);

    void createNewUser(String tokenEmail, CreateNewUserRequest request);

    void deleteUser(Integer id);

    void updateRole(String email, String role, String tokenEmail);

    void updateStatus(String email, String status, String tokenEmail);
}
