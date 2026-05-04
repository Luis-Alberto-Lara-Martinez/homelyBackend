package org.educa.homelyBackend.services.business;

import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.daos.UserDao;
import org.educa.homelyBackend.models.UserModel;
import org.educa.homelyBackend.services.business.impl.UserRoleServiceImpl;
import org.educa.homelyBackend.services.business.impl.UserStatusServiceImpl;
import org.educa.homelyBackend.services.shared.impl.AvatarServiceImpl;
import org.educa.homelyBackend.services.shared.impl.CloudinaryServiceImpl;
import org.educa.homelyBackend.services.shared.impl.PasswordEncoderServiceImpl;
import org.educa.homelyBackend.services.shared.impl.ResendServiceImpl;
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
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final UserRoleServiceImpl userRoleServiceImpl;
    private final UserStatusServiceImpl userStatusServiceImpl;
    private final ResendServiceImpl resendServiceImpl;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;
    private final AvatarServiceImpl avatarServiceImpl;
    private final PasswordEncoderServiceImpl passwordEncoderServiceImpl;

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
        user.setRole(userRoleServiceImpl.findByNameOrThrow("USER"));
        user.setStatus(userStatusServiceImpl.findByNameOrThrow("ACTIVE"));
        user.setName(name);
        user.setEmail(email);

        if (password != null && !password.isBlank()) {
            user.setHashedPassword(passwordEncoderServiceImpl.generateHashedPassword(password));
        }

        user = saveOrUpdate(user);

        String imageUrl = cloudinaryServiceImpl.uploadAvatarImage(avatarServiceImpl.generateAvatar(name), user.getId());
        user.setImageUrl(imageUrl);

        resendServiceImpl.sendWelcomeEmail(email, name);

        return saveOrUpdate(user);
    }

    public void updateHashedPassword(String email, String password) {
        UserModel user = findByEmailOrThrow(email);
        user.setHashedPassword(passwordEncoderServiceImpl.generateHashedPassword(password));
        saveOrUpdate(user);
    }

    public void updateHashedPassword(UserModel user, String password) {
        user.setHashedPassword(passwordEncoderServiceImpl.generateHashedPassword(password));
        saveOrUpdate(user);
    }

    public void checkHashedPassword(String password, String hashedPassword) {
        if (!passwordEncoderServiceImpl.checkHashedPassword(password, hashedPassword)) {
            throw ExceptionUtil.manageException(HttpStatus.BAD_REQUEST, "The password is incorrect").get();
        }
    }

    public String updateImage(String email, MultipartFile avatarFile) {
        UserModel user = findByEmailOrThrow(email);

        user.setImageUrl(cloudinaryServiceImpl.uploadAvatarImage(avatarFile, user.getId()));

        return saveOrUpdate(user).getImageUrl();
    }

    public String updateName(String email, String name) {
        UserModel user = findByEmailOrThrow(email);

        user.setName(name);

        return saveOrUpdate(user).getName();
    }
}
