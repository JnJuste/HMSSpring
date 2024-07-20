package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
}
