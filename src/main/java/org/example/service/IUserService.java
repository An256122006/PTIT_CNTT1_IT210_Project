package org.example.service;

import org.example.dto.UsersDto;
import org.example.model.Users;

import java.util.List;

public interface IUserService {
    void save(UsersDto usersDto);
    boolean islogin(String username, String password);
    List<Users> findAll();
    Users findByUsername(String username);
    Users findById(Long id);
}