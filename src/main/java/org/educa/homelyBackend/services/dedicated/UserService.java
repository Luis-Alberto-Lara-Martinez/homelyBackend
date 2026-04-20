package org.educa.homelyBackend.services.dedicated;

import com.resend.core.exception.ResendException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.educa.homelyBackend.daos.UsersRepository;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.models.UserRoleModel;
import org.educa.homelyBackend.models.UserStatusModel;
import org.educa.homelyBackend.services.common.AvatarService;
import org.educa.homelyBackend.services.common.CloudinaryService;
import org.educa.homelyBackend.services.common.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.Optional;

@Service
@Validated
public class UserService {

    private final UsersRepository usersRepository;
    private final UserRoleService userRoleService;
    private final UserStatusService userStatusService;
    private final PasswordEncoderService passwordEncoderService;
    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;
    private final AvatarService avatarService;

    public UserService(UsersRepository usersRepository, UserRoleService userRoleService, UserStatusService userStatusService, PasswordEncoderService passwordEncoderService, EmailService emailService, CloudinaryService cloudinaryService, AvatarService avatarService) {
        this.usersRepository = usersRepository;
        this.userRoleService = userRoleService;
        this.userStatusService = userStatusService;
        this.passwordEncoderService = passwordEncoderService;
        this.emailService = emailService;
        this.cloudinaryService = cloudinaryService;
        this.avatarService = avatarService;
    }

    public Page<UserModel> findAll(Integer page, Integer size) {
        return findAll(page, size, null);
    }

    public Page<UserModel> findAll(Integer page, Integer size, String sortBy) {
        int pageNumber = (page == null || page < 0)
                ? 0
                : page;

        int sizeNumber = (size == null || size < 0)
                ? 30
                : size;

        String sortParameter = (sortBy == null || sortBy.isBlank())
                ? "id"
                : sortBy;

        return usersRepository.findAll(PageRequest.of(pageNumber, sizeNumber, Sort.by(sortParameter).ascending()));
    }

    public Optional<UserModel> findByEmail(
            @NotBlank(message = "El email no puede estar vacío")
            @Email(message = "Formato de email inválido")
            String email
    ) {
        return usersRepository.findByEmail(email);
    }

    public UserModel saveOrUpdate(@NotNull UserModel user) {
        return usersRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<UserModel> findOrCreate(
            @NotBlank(message = "El nombre no puede estar vacío")
            String name,

            @NotBlank(message = "El email no puede estar vacío")
            @Email(message = "Formato de email inválido")
            String email,

            String password
    ) throws IOException, ResendException {
        Optional<UserModel> userSearched = findByEmail(email);

        if (userSearched.isPresent()) {
            return userSearched;
        }

        Optional<UserRoleModel> userRol = userRoleService.findByName("USER");
        Optional<UserStatusModel> userStatus = userStatusService.findByName("ACTIVE");

        if (userRol.isEmpty() || userStatus.isEmpty()) {
            return Optional.empty();
        }

        UserModel newUser = new UserModel();
        newUser.setRole(userRol.get());
        newUser.setStatus(userStatus.get());
        newUser.setName(name);
        newUser.setEmail(email);

        if (password != null && !password.isBlank()) {
            newUser.setHashedPassword(passwordEncoderService.generateHash(password));
        }

        newUser = saveOrUpdate(newUser);

        String imageUrl = cloudinaryService.uploadAvatarImage(avatarService.generateAvatar(name), newUser.getId());
        newUser.setImageUrl(imageUrl);

        emailService.sendWelcomeEmail(email, name);

        return Optional.of(saveOrUpdate(newUser));
    }
}