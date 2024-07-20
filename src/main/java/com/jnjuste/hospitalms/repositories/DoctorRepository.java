package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID>{
}
