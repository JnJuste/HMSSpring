package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID>{
    Optional<Doctor> findByEmail(String email);
    Optional<Doctor> findByNationalID(Integer nationalID);
    Optional<Doctor> findByRegNumber(String regNumber);
}
