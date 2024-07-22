package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.PatientNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientNumberRepository extends JpaRepository<PatientNumberSequence, Long> {
}
