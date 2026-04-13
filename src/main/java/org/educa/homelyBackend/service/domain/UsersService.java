package org.educa.homelyBackend.service.domain;

import com.resend.core.exception.ResendException;
import org.educa.homelyBackend.entity.UserRoles;
import org.educa.homelyBackend.entity.UserStatuses;
import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.repository.UsersRepository;
import org.educa.homelyBackend.service.common.AvatarService;
import org.educa.homelyBackend.service.common.CloudinaryService;
import org.educa.homelyBackend.service.common.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRolesService userRolesService;
    private final UserStatusesService userStatusesService;
    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;
    private final AvatarService avatarService;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, UserRolesService userRolesService, UserStatusesService userStatusesService, EmailService emailService, CloudinaryService cloudinaryService, AvatarService avatarService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRolesService = userRolesService;
        this.userStatusesService = userStatusesService;
        this.emailService = emailService;
        this.cloudinaryService = cloudinaryService;
        this.avatarService = avatarService;
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public Optional<Users> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<Users> processNewUser(String name, String email, String password) throws ResendException, IOException {
        Optional<UserRoles> userRol = userRolesService.findByName("USER");
        Optional<UserStatuses> userStatus = userStatusesService.findByName("ACTIVE");

        if (userRol.isEmpty()) return Optional.empty();
        if (userStatus.isEmpty()) return Optional.empty();

        String newImageUrl = cloudinaryService.uploadFile(avatarService.generateAvatar(name), email);

        Users newUser = new Users();

        newUser.setIdRole(userRol.get());
        newUser.setIdStatus(userStatus.get());
        newUser.setImageUrl(newImageUrl);
        newUser.setName(name);
        newUser.setEmail(email);
        if (password != null && !password.isBlank()) newUser.setHashPassword(encodePassword(password));

        saveUser(newUser);
        emailService.sendWelcomeEmail(email, name);
        return Optional.of(newUser);
    }

    public void saveUser(Users user) {
        usersRepository.save(user);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}