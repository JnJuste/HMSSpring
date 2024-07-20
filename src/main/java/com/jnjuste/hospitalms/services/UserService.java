package com.jnjuste.hospitalms.services;

import com.jnjuste.hospitalms.models.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User saveUser(User user);
    User getUserById(UUID id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    User updateUser(UUID id, User user);
    void deleteUser(UUID id);
}
