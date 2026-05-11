package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.UsersDto;
import org.example.model.Role;
import org.example.model.Status;
import org.example.model.Users;
import org.example.repository.UserRepository;
import org.example.service.IUserProfileService;
import org.example.service.IUserService;
import org.example.service.PasswordService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository usersRepository;
    private final PasswordService passwordService;
    @Override
    public void save(UsersDto usersDto) {
        Users users = new Users();
        users.setUsername(usersDto.getUsername());
        users.setPassword(passwordService.hash(usersDto.getPassword()));
        users.setRole(Role.PATIENT);
        users.setStatus(Status.ACTIVE);
        usersRepository.save(users);
    }
    @Override
    public boolean islogin(String username, String password) {
        Users users = usersRepository.findByUsername(username);
        return passwordService.verify(password, users.getPassword());
    }
    @Override
    public List<Users> findAll() {
        return usersRepository.findAll();
    }
    @Override
    public Users findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }
    @Override
    public Users findById(Long id) {
        return usersRepository.findById(id).orElse(null);
    }
}
