package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NurseRepository extends JpaRepository<Nurse,UUID> {
}
