package org.educa.homelyBackend.service.domain;

import org.educa.homelyBackend.entity.Users;
import org.educa.homelyBackend.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
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
}