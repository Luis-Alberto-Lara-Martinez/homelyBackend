package org.educa.homelyBackend.service.domain;

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

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public Optional<Users> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public void save(Users user) {
        usersRepository.save(user);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}