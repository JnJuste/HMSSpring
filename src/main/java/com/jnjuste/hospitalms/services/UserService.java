package com.jnjuste.hospitalms.services;

import com.jnjuste.hospitalms.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User saveUser(User user);
    List<User> getAllUsers();
    Optional<User> getUserById(UUID id);
    Optional<User> getUserByEmail(String email);

    User updateUser(UUID id, User userDetails);

    void deleteUser(UUID id);
}
