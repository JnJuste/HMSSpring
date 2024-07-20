package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
}
