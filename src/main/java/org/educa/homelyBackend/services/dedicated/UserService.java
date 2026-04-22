package org.educa.homelyBackend.services.dedicated;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.educa.homelyBackend.daos.UserDao;
import org.educa.homelyBackend.models.UserModel;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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

    public Page<UserModel> findAll(@NotNull(message = "The page cannot be null") @Min(value = 1, message = "The page must be greater than or equal to 1") Integer page,

                                   @NotNull @Min(value = 1, message = "The size must be greater than or equal to 1") Integer size) {
        return findAll(page, size, null);
    }

    public Page<UserModel> findAll(@NotNull(message = "The page cannot be null") @Min(value = 1, message = "The page must be greater than or equal to 1") Integer page,

                                   @NotNull @Min(value = 1, message = "The size must be greater than or equal to 1") Integer size,

                                   String sortBy) {
        String sortParameter = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;

        Page<UserModel> pagedUsers = userDao.findAll(PageRequest.of(page - 1, size, Sort.by(sortParameter).ascending()));

        if (pagedUsers.isEmpty()) {
            throw ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "No users found").get();
        }

        return pagedUsers;
    }

    public Optional<UserModel> findByEmail(@NotBlank(message = "El email no puede estar vacío") @Email(message = "Formato de email inválido") String email) {
        return userDao.findByEmail(email);
    }

    public UserModel findByEmailOrThrow(@NotBlank(message = "El email no puede estar vacío") @Email(message = "Formato de email inválido") String email) {
        return findByEmail(email).orElseThrow(() -> ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "There is no user with that email").get());
    }

    public UserModel saveOrUpdate(@NotNull(message = "The user cannot be null") UserModel user) {
        return userDao.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserModel createAndSendWelcomeEmail(@NotBlank(message = "El nombre no puede estar vacío") String name,

                                               @NotBlank(message = "El email no puede estar vacío") @Email(message = "Formato de email inválido") String email) {
        return createAndSendWelcomeEmail(name, email, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserModel createAndSendWelcomeEmail(@NotBlank(message = "El nombre no puede estar vacío") String name,

                                               @NotBlank(message = "El email no puede estar vacío") @Email(message = "Formato de email inválido") String email,

                                               String password) {
        if (findByEmail(email).isPresent()) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The email is already register").get();
        }

        UserModel user = UserModel.builder().role(userRoleService.findByNameOrThrow("USER")).status(userStatusService.findByNameOrThrow("ACTIVE")).name(name).email(email).build();

        if (password != null && !password.isBlank()) {
            user.setHashedPassword(encoderService.generateHashedPassword(password));
        }

        user = saveOrUpdate(user);

        String imageUrl = cloudinaryService.uploadAvatarImage(avatarService.generateAvatar(name), user.getId());
        user.setImageUrl(imageUrl);

        emailService.sendWelcomeEmail(email, name);

        return saveOrUpdate(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateProfile(String email, MultipartFile avatarFile, String name, String password, String confirmedPassword) {

        UserModel user = findByEmailOrThrow(email);

        boolean makeChanges = false;

        if (name != null && !name.trim().equals(user.getName())) {
            user.setName(name.trim());
            makeChanges = true;
        }
        aaaa

        if (password != null) {
            if (password.equals(confirmedPassword)) {
                user.setHashedPassword(encoderService.generateHashedPassword(password));
                makeChanges = true;
            } else {
                throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The passwords do not match").get();
            }
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            user.setImageUrl(cloudinaryService.uploadAvatarImage(avatarFile, user.getId()));
            makeChanges = true;
        }

        if (makeChanges) {
            saveOrUpdate(user);
        }

        return makeChanges;
    }

    public void updateHashedPassword(@NotNull(message = "The user cannot be null") UserModel user,

                                     @NotBlank(message = "The password cannot be null nor empty") String password) {
        user.setHashedPassword(encoderService.generateHashedPassword(password));
        saveOrUpdate(user);
    }

    public void checkHashedPassword(String password, String hashedPassword) {
        if (!encoderService.checkHashedPassword(password, hashedPassword)) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The password is incorrect").get();
        }
    }
}
