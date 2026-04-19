package org.educa.homelyBackend.services.dedicated;

import com.resend.core.exception.ResendException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.educa.homelyBackend.daos.UsersRepository;
import org.educa.homelyBackend.services.common.AvatarService;
import org.educa.homelyBackend.services.common.CloudinaryService;
import org.educa.homelyBackend.services.common.EmailService;
import org.educa.homelyBackend.services.common.PasswordEncoderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoderService passwordEncoder;
    private final UserRolesService userRolesService;
    private final UserStatusesService userStatusesService;
    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;
    private final AvatarService avatarService;

    public UsersService(UsersRepository usersRepository, PasswordEncoderService passwordEncoder, UserRolesService userRolesService, UserStatusesService userStatusesService, EmailService emailService, CloudinaryService cloudinaryService, AvatarService avatarService) {
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

    public Optional<Users> findByEmail(
            @NotBlank(message = "El email no puede estar vacío")
            @Email(message = "Formato de email inválido")
            String email
    ) {
        return usersRepository.findByEmail(email);
    }

    public void saveUser(@NotNull Users user) {
        usersRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<Users> getUserOrCreateNewUser(
            @NotBlank(message = "El nombre no puede estar vacío")
            String name,

            @NotBlank(message = "El email no puede estar vacío")
            @Email(message = "Formato de email inválido")
            String email,

            String password
    ) throws ResendException, IOException {
        Optional<Users> userSearched = findByEmail(email);

        if (userSearched.isEmpty()) {
            emailService.sendWelcomeEmail(email, name);
            return createNewUser(name, email, password);
        } else {
            return userSearched;
        }
    }

    private Optional<Users> createNewUser(String name, String email, String password) throws IOException {
        Optional<UserRoles> userRol = userRolesService.findByName("USER");
        Optional<UserStatuses> userStatus = userStatusesService.findByName("ACTIVE");

        if (userRol.isEmpty() || userStatus.isEmpty()) return Optional.empty();

        Users newUser = new Users();
        newUser.setIdRole(userRol.get());
        newUser.setIdStatus(userStatus.get());
        newUser.setName(name);
        newUser.setEmail(email);
        if (password != null && !password.isBlank()) newUser.setHashPassword(passwordEncoder.generateHash(password));

        newUser = usersRepository.save(newUser);

        String newImageUrl = cloudinaryService.uploadAvatarImage(avatarService.generateAvatar(name), newUser.getId());
        newUser.setImageUrl(newImageUrl);

        return Optional.of(usersRepository.save(newUser));
    }
}