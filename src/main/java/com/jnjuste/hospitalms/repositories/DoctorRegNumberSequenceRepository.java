package com.jnjuste.hospitalms.repositories;

import com.jnjuste.hospitalms.models.DoctorRegNumberSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRegNumberSequenceRepository extends JpaRepository<DoctorRegNumberSequence, Long> {
}
