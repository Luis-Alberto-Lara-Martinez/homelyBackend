package org.educa.homelyBackend.facades.admin.impl;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.dtos.responses.FindAllUsersResponse;
import org.educa.homelyBackend.facades.admin.UserAdminFacade;
import org.educa.homelyBackend.services.business.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdminFacadeImpl implements UserAdminFacade {

    private final UserService userService;

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
}


