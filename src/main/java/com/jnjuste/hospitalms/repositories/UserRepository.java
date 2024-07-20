package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);

}
