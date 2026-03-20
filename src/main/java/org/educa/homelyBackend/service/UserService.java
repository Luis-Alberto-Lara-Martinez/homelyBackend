package org.educa.homelyBackend.service;

import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}