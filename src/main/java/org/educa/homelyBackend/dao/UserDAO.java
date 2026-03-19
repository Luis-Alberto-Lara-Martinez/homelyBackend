package org.educa.homelyBackend.dao;

import org.educa.homelyBackend.entity.Users;

import java.util.List;

public interface UserDAO {
    List<Users> findAll();
}