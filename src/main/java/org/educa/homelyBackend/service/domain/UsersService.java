package org.educa.homelyBackend.service.domain;

import org.educa.homelyBackend.entity.UserRoles;
import org.educa.homelyBackend.entity.UserStatuses;
import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.repository.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRolesService userRolesService;
    private final UserStatusesService userStatusesService;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, UserRolesService userRolesService, UserStatusesService userStatusesService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRolesService = userRolesService;
        this.userStatusesService = userStatusesService;
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public Optional<Users> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public Optional<Users> processNewUser(String name, String email, String password) {
        Optional<UserRoles> userRol = userRolesService.findByName("USER");
        Optional<UserStatuses> userStatus = userStatusesService.findByName("ACTIVE");

        if (userRol.isEmpty()) return Optional.empty();
        if (userStatus.isEmpty()) return Optional.empty();

        Users newUser = new Users();
        newUser.setIdRole(userRol.get());
        newUser.setIdStatus(userStatus.get());
        newUser.setImageUrl("https://res.cloudinary.com/homely-cloudinary/image/upload/v1767874172/perfil.png");
        newUser.setName(name);
        newUser.setEmail(email);
        if (password != null && !password.isBlank()) {
            newUser.setHashPassword(passwordEncoder.encode(password));
        }

        save(newUser);

        return Optional.of(newUser);
    }

    public void save(Users user) {
        usersRepository.save(user);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}