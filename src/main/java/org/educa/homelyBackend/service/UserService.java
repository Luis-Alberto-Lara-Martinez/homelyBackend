package org.educa.homelyBackend.service;

import org.educa.homelyBackend.dao.UserDAO;
import org.educa.homelyBackend.dao.UserDAOImpl;
import org.educa.homelyBackend.entity.Users;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    public List<Users> findAll() {
        return userDAO.findAll();
    }
}