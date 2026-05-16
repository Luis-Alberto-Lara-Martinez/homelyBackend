package org.educa.homelyBackend.facades.admin;

import org.educa.homelyBackend.dtos.responses.FindAllUsersResponse;
import org.springframework.data.domain.Page;

public interface UserAdminFacade {
    Page<FindAllUsersResponse> findAll(Integer page, Integer size, String sortBy);
}
