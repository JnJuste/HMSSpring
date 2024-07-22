package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface NurseRepository extends JpaRepository<Nurse,UUID> {
    Optional<Nurse> findByEmail(String email);
    Optional<Nurse> findByNationalID(Integer nationalID);
    Optional<Nurse> findByRegNumber(String regNumber);
}
