package org.educa.homelyBackend.services.dedicated;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.educa.homelyBackend.daos.UserDao;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.models.UserRoleModel;
import org.educa.homelyBackend.models.UserStatusModel;
import org.educa.homelyBackend.services.common.AvatarService;
import org.educa.homelyBackend.services.common.CloudinaryService;
import org.educa.homelyBackend.services.common.EmailService;
import org.educa.homelyBackend.services.common.EncoderService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UserService {

    private final UserDao userDao;
    private final UserRoleService userRoleService;
    private final UserStatusService userStatusService;
    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;
    private final AvatarService avatarService;
    private final EncoderService encoderService;

    public UserService(UserDao userDao, UserRoleService userRoleService, UserStatusService userStatusService, EmailService emailService, CloudinaryService cloudinaryService, AvatarService avatarService, EncoderService encoderService) {
        this.userDao = userDao;
        this.userRoleService = userRoleService;
        this.userStatusService = userStatusService;
        this.emailService = emailService;
        this.cloudinaryService = cloudinaryService;
        this.avatarService = avatarService;
        this.encoderService = encoderService;
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

        Page<UserModel> pagedUsers = userDao.findAll(PageRequest.of(pageNumber, sizeNumber, Sort.by(sortParameter).ascending()));

        if (pagedUsers.isEmpty()) {
            throw ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "No users found").get();
        }

        return pagedUsers;
    }

    public UserModel findByEmail(
            @NotBlank(message = "El email no puede estar vacío")
            @Email(message = "Formato de email inválido")
            String email
    ) {
        return userDao
                .findByEmail(email)
                .orElseThrow(ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "User not found with email: " + email));
    }

    public UserModel saveOrUpdate(@NotNull UserModel user) {
        return userDao.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserModel createAndSendWelcomeEmail(
            @NotBlank(message = "El nombre no puede estar vacío")
            String name,

            @NotBlank(message = "El email no puede estar vacío")
            @Email(message = "Formato de email inválido")
            String email,

            String password
    ) {
        UserRoleModel rol = userRoleService.findByName("USER");
        UserStatusModel status = userStatusService.findByName("ACTIVE");

        UserModel user = new UserModel();
        user.setRole(rol);
        user.setStatus(status);
        user.setName(name);
        user.setEmail(email);

        if (password != null && !password.isBlank()) {
            user.setHashedPassword(encoderService.generateHashPassword(password));
        }

        user = saveOrUpdate(user);

        String imageUrl = cloudinaryService.uploadAvatarImage(avatarService.generateAvatar(name), user.getId());
        user.setImageUrl(imageUrl);

        emailService.sendWelcomeEmail(email, name);

        return saveOrUpdate(user);
    }
}
