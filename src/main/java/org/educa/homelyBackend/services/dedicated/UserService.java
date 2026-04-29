package org.educa.homelyBackend.services.dedicated;

import org.educa.homelyBackend.daos.UserDao;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.common.AvatarService;
import org.educa.homelyBackend.services.common.CloudinaryService;
import org.educa.homelyBackend.services.common.ResendService;
import org.educa.homelyBackend.services.common.EncoderService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;
    private final UserRoleService userRoleService;
    private final UserStatusService userStatusService;
    private final ResendService resendService;
    private final CloudinaryService cloudinaryService;
    private final AvatarService avatarService;
    private final EncoderService encoderService;

    public UserService(UserDao userDao, UserRoleService userRoleService, UserStatusService userStatusService, ResendService resendService, CloudinaryService cloudinaryService, AvatarService avatarService, EncoderService encoderService) {
        this.userDao = userDao;
        this.userRoleService = userRoleService;
        this.userStatusService = userStatusService;
        this.resendService = resendService;
        this.cloudinaryService = cloudinaryService;
        this.avatarService = avatarService;
        this.encoderService = encoderService;
    }

    public Page<UserModel> findAll(Integer page, Integer size) {
        return findAll(page, size, null);
    }

    public Page<UserModel> findAll(Integer page, Integer size, String sortBy) {
        String sortParameter = (sortBy == null || sortBy.isBlank())
                ? "id"
                : sortBy;

        Page<UserModel> pagedUsers = userDao.findAll(PageRequest.of(page - 1, size, Sort.by(sortParameter).ascending()));

        if (pagedUsers.isEmpty()) {
            throw ExceptionUtil.manageException(HttpStatus.NOT_FOUND, "No users found").get();
        }

        return pagedUsers;
    }

    public Optional<UserModel> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public UserModel findByEmailOrThrow(String email) {
        return findByEmail(email).orElseThrow(() -> ExceptionUtil.manageException(
                HttpStatus.NOT_FOUND,
                "There is no user with that email"
        ).get());
    }

    public UserModel saveOrUpdate(UserModel user) {
        return userDao.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserModel createAndSendWelcomeEmail(String name, String email) {
        return createAndSendWelcomeEmail(name, email, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserModel createAndSendWelcomeEmail(String name, String email, String password) {
        if (findByEmail(email).isPresent()) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The email is already register").get();
        }

        UserModel user = new UserModel();
        user.setRole(userRoleService.findByNameOrThrow("USER"));
        user.setStatus(userStatusService.findByNameOrThrow("ACTIVE"));
        user.setName(name);
        user.setEmail(email);

        if (password != null && !password.isBlank()) {
            user.setHashedPassword(encoderService.generateHashedPassword(password));
        }

        user = saveOrUpdate(user);

        String imageUrl = cloudinaryService.uploadAvatarImage(avatarService.generateAvatar(name), user.getId());
        user.setImageUrl(imageUrl);

        resendService.sendWelcomeEmail(email, name);

        return saveOrUpdate(user);
    }

    public void updateHashedPassword(String email, String password) {
        UserModel user = findByEmailOrThrow(email);
        user.setHashedPassword(encoderService.generateHashedPassword(password));
        saveOrUpdate(user);
    }

    public void updateHashedPassword(UserModel user, String password) {
        user.setHashedPassword(encoderService.generateHashedPassword(password));
        saveOrUpdate(user);
    }

    public void checkHashedPassword(String password, String hashedPassword) {
        if (!encoderService.checkHashedPassword(password, hashedPassword)) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The password is incorrect").get();
        }
    }

    public String updateImage(String email, MultipartFile avatarFile) {
        UserModel user = findByEmailOrThrow(email);

        user.setImageUrl(cloudinaryService.uploadAvatarImage(avatarFile, user.getId()));

        return saveOrUpdate(user).getImageUrl();
    }

    public String updateName(String email, String name) {
        UserModel user = findByEmailOrThrow(email);

        user.setName(name);

        return saveOrUpdate(user).getName();
    }
}
